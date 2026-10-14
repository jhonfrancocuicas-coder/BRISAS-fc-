package com.example.ui.viewmodel

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.InitialData
import com.example.data.model.Match
import com.example.data.model.Team
import com.example.data.repository.TournamentRepository
import com.example.ui.auth.AuthHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TournamentTab {
  STANDINGS,
  FIXTURES,
  TEAMS,
  ADMIN
}

data class TournamentUiState(
  val currentTab: TournamentTab = TournamentTab.STANDINGS,
  val currentUser: FirebaseUser? = null,
  val teams: List<Team> = InitialData.getDefaultTeams(),
  val matches: List<Match> = InitialData.getDefaultMatches(),
  val isLoading: Boolean = false,
  val userMessage: String? = null,
  val selectedTeamDetail: Team? = null
)

class TournamentViewModel(
  private val repository: TournamentRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(TournamentUiState())
  val uiState: StateFlow<TournamentUiState> = _uiState.asStateFlow()

  private val auth = FirebaseAuth.getInstance()
  private var authStateListener: FirebaseAuth.AuthStateListener? = null

  init {
    setupAuthObservation()
  }

  private fun setupAuthObservation() {
    authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
      val user = firebaseAuth.currentUser
      _uiState.update { it.copy(currentUser = user) }
      if (user != null) {
        onUserAuthenticated(user.uid)
      } else {
        // Return to local default data when not signed in
        _uiState.update {
          it.copy(
            teams = InitialData.getDefaultTeams(),
            matches = InitialData.getDefaultMatches()
          )
        }
      }
    }
    authStateListener?.let { auth.addAuthStateListener(it) }
  }

  private fun onUserAuthenticated(userId: String) {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }
      try {
        repository.seedInitialDataIfEmpty(userId)
      } catch (e: Exception) {
        // Handled in repository
      } finally {
        _uiState.update { it.copy(isLoading = false) }
      }

      // Observe teams from Firestore
      launch {
        repository.observeTeams(userId)
          .catch { e ->
            _uiState.update { it.copy(userMessage = "Error al sincronizar equipos") }
          }
          .collectLatest { remoteTeams ->
            if (remoteTeams.isNotEmpty()) {
              _uiState.update { it.copy(teams = remoteTeams) }
            }
          }
      }

      // Observe matches from Firestore
      launch {
        repository.observeMatches(userId)
          .catch { e ->
            _uiState.update { it.copy(userMessage = "Error al sincronizar partidos") }
          }
          .collectLatest { remoteMatches ->
            if (remoteMatches.isNotEmpty()) {
              _uiState.update { it.copy(matches = remoteMatches) }
            }
          }
      }
    }
  }

  fun selectTab(tab: TournamentTab) {
    _uiState.update { it.copy(currentTab = tab) }
  }

  fun showTeamDetail(team: Team?) {
    _uiState.update { it.copy(selectedTeamDetail = team) }
  }

  fun clearMessage() {
    _uiState.update { it.copy(userMessage = null) }
  }

  fun updateMatchScore(match: Match, homeScore: Int, awayScore: Int, isFinished: Boolean) {
    val user = auth.currentUser ?: return
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }
      try {
        val updated = match.copy(
          homeScore = homeScore,
          awayScore = awayScore,
          status = if (isFinished) "finished" else "scheduled"
        )
        repository.updateMatch(user.uid, updated)
        if (isFinished) {
          repository.recalculateStandingsFromMatches(user.uid)
        }
        _uiState.update { it.copy(userMessage = "Marcador guardado con éxito") }
      } catch (e: Exception) {
        _uiState.update { it.copy(userMessage = "Error al guardar marcador: ${e.localizedMessage}") }
      } finally {
        _uiState.update { it.copy(isLoading = false) }
      }
    }
  }

  fun recalculateStandings() {
    val user = auth.currentUser ?: return
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }
      try {
        repository.recalculateStandingsFromMatches(user.uid)
        _uiState.update { it.copy(userMessage = "Tabla de posiciones recalculada") }
      } catch (e: Exception) {
        _uiState.update { it.copy(userMessage = "Error al recalcular: ${e.localizedMessage}") }
      } finally {
        _uiState.update { it.copy(isLoading = false) }
      }
    }
  }

  fun resetTournament() {
    val user = auth.currentUser ?: return
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }
      try {
        repository.resetTournament(user.uid)
        _uiState.update { it.copy(userMessage = "Torneo reiniciado exitosamente") }
      } catch (e: Exception) {
        _uiState.update { it.copy(userMessage = "Error al reiniciar: ${e.localizedMessage}") }
      } finally {
        _uiState.update { it.copy(isLoading = false) }
      }
    }
  }

  fun signOut(context: Context, credentialManager: CredentialManager) {
    AuthHelper.signOut(
      context = context,
      credentialManager = credentialManager,
      onSignOutComplete = {
        _uiState.update {
          it.copy(
            currentUser = null,
            userMessage = "Sesión cerrada correctamente"
          )
        }
      },
      scope = viewModelScope
    )
  }

  override fun onCleared() {
    super.onCleared()
    authStateListener?.let { auth.removeAuthStateListener(it) }
  }
}
