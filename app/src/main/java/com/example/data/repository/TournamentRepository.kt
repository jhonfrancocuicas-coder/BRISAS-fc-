package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.InitialData
import com.example.data.model.Match
import com.example.data.model.Team
import com.example.data.util.OperationType
import com.example.data.util.handleFirestoreError
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class TournamentRepository(private val db: FirebaseFirestore) {

  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      FirebaseApp.getInstance(),
      context.applicationContext.getString(R.string.firestore_database_id)
    )
  )

  private val auth = FirebaseAuth.getInstance()

  private fun requireUserId(): String {
    return auth.currentUser?.uid
      ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
  }

  fun observeTeams(userId: String): Flow<List<Team>> = flow {
    val path = "users/$userId/teams"
    emitAll(
      db.collection("users").document(userId).collection("teams")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(Team::class.java).sortedWith(
            compareByDescending<Team> { it.pts }
              .thenByDescending { it.dg }
              .thenByDescending { it.gf }
              .thenBy { it.name }
          ).mapIndexed { index, team ->
            team.copy(position = index + 1)
          }
        }
        .catch { error ->
          if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
          throw error
        }
    )
  }

  fun observeMatches(userId: String): Flow<List<Match>> = flow {
    val path = "users/$userId/matches"
    emitAll(
      db.collection("users").document(userId).collection("matches")
        .snapshots()
        .map { snapshot ->
          snapshot.toObjects(Match::class.java).sortedWith(
            compareBy<Match> { it.round }.thenBy { it.id }
          )
        }
        .catch { error ->
          if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
          throw error
        }
    )
  }

  suspend fun seedInitialDataIfEmpty(userId: String) {
    val currentUid = requireUserId()
    if (currentUid != userId) {
      throw IllegalStateException("Cannot seed data for another user")
    }
    val teamsRef = db.collection("users").document(userId).collection("teams")
    val path = teamsRef.path
    try {
      val existing = teamsRef.limit(1).get().await()
      if (!existing.isEmpty) return

      val defaultTeams = InitialData.getDefaultTeams()
      val defaultMatches = InitialData.getDefaultMatches()

      val batch = db.batch()
      defaultTeams.forEach { team ->
        val doc = teamsRef.document(team.id)
        val payload = mapOf(
          "name" to team.name,
          "shortName" to team.shortName,
          "shieldColor" to team.shieldColor,
          "accentColor" to team.accentColor,
          "badgeIcon" to team.badgeIcon,
          "position" to team.position,
          "pj" to team.pj,
          "g" to team.g,
          "e" to team.e,
          "p" to team.p,
          "gf" to team.gf,
          "gc" to team.gc,
          "dg" to team.dg,
          "pts" to team.pts,
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp()
        )
        batch.set(doc, payload)
      }

      val matchesRef = db.collection("users").document(userId).collection("matches")
      defaultMatches.forEach { match ->
        val doc = matchesRef.document(match.id)
        val payload = mapOf(
          "round" to match.round,
          "homeTeamId" to match.homeTeamId,
          "homeTeamName" to match.homeTeamName,
          "awayTeamId" to match.awayTeamId,
          "awayTeamName" to match.awayTeamName,
          "homeScore" to match.homeScore,
          "awayScore" to match.awayScore,
          "status" to match.status,
          "matchDate" to match.matchDate,
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp()
        )
        batch.set(doc, payload)
      }

      batch.commit().await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, path)
      throw e
    }
  }

  suspend fun updateTeam(userId: String, team: Team) {
    val currentUid = requireUserId()
    if (currentUid != userId) throw IllegalStateException("Access denied")
    require(team.id.isNotBlank()) { "Team ID must not be blank" }
    val docRef = db.collection("users").document(userId).collection("teams").document(team.id)
    try {
      val existingDoc = docRef.get().await()
      val createdAt = existingDoc.getTimestamp("createdAt") ?: FieldValue.serverTimestamp()
      val payload = mapOf(
        "name" to team.name,
        "shortName" to team.shortName,
        "shieldColor" to team.shieldColor,
        "accentColor" to team.accentColor,
        "badgeIcon" to team.badgeIcon,
        "position" to team.position,
        "pj" to team.pj,
        "g" to team.g,
        "e" to team.e,
        "p" to team.p,
        "gf" to team.gf,
        "gc" to team.gc,
        "dg" to team.dg,
        "pts" to team.pts,
        "createdAt" to createdAt,
        "updatedAt" to FieldValue.serverTimestamp()
      )
      docRef.set(payload).await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, docRef.path)
      throw e
    }
  }

  suspend fun updateMatch(userId: String, match: Match) {
    val currentUid = requireUserId()
    if (currentUid != userId) throw IllegalStateException("Access denied")
    require(match.id.isNotBlank()) { "Match ID must not be blank" }
    val docRef = db.collection("users").document(userId).collection("matches").document(match.id)
    try {
      val existingDoc = docRef.get().await()
      val createdAt = existingDoc.getTimestamp("createdAt") ?: FieldValue.serverTimestamp()
      val payload = mapOf(
        "round" to match.round,
        "homeTeamId" to match.homeTeamId,
        "homeTeamName" to match.homeTeamName,
        "awayTeamId" to match.awayTeamId,
        "awayTeamName" to match.awayTeamName,
        "homeScore" to match.homeScore,
        "awayScore" to match.awayScore,
        "status" to match.status,
        "matchDate" to match.matchDate,
        "createdAt" to createdAt,
        "updatedAt" to FieldValue.serverTimestamp()
      )
      docRef.set(payload).await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, docRef.path)
      throw e
    }
  }

  suspend fun recalculateStandingsFromMatches(userId: String) {
    val currentUid = requireUserId()
    if (currentUid != userId) throw IllegalStateException("Access denied")
    val teamsRef = db.collection("users").document(userId).collection("teams")
    val matchesRef = db.collection("users").document(userId).collection("matches")
    try {
      val teamsSnapshot = teamsRef.get().await()
      val matchesSnapshot = matchesRef.get().await()

      val teams = teamsSnapshot.toObjects(Team::class.java).associateBy { it.id }.toMutableMap()
      val matches = matchesSnapshot.toObjects(Match::class.java)

      // Reset team stats
      teams.keys.forEach { teamId ->
        val existing = teams[teamId]!!
        teams[teamId] = existing.copy(pj = 0, g = 0, e = 0, p = 0, gf = 0, gc = 0, dg = 0, pts = 0)
      }

      // Process finished matches
      for (match in matches) {
        if (match.status != "finished") continue
        val home = teams[match.homeTeamId] ?: continue
        val away = teams[match.awayTeamId] ?: continue

        val homePj = home.pj + 1
        val awayPj = away.pj + 1
        val homeGf = home.gf + match.homeScore
        val homeGc = home.gc + match.awayScore
        val awayGf = away.gf + match.awayScore
        val awayGc = away.gc + match.homeScore

        val (homeG, homeE, homeP, homePts) = when {
          match.homeScore > match.awayScore -> listOf(home.g + 1, home.e, home.p, home.pts + 3)
          match.homeScore == match.awayScore -> listOf(home.g, home.e + 1, home.p, home.pts + 1)
          else -> listOf(home.g, home.e, home.p + 1, home.pts)
        }

        val (awayG, awayE, awayP, awayPts) = when {
          match.awayScore > match.homeScore -> listOf(away.g + 1, away.e, away.p, away.pts + 3)
          match.awayScore == match.homeScore -> listOf(away.g, away.e + 1, away.p, away.pts + 1)
          else -> listOf(away.g, away.e, away.p + 1, away.pts)
        }

        teams[home.id] = home.copy(
          pj = homePj, g = homeG, e = homeE, p = homeP,
          gf = homeGf, gc = homeGc, dg = homeGf - homeGc, pts = homePts
        )
        teams[away.id] = away.copy(
          pj = awayPj, g = awayG, e = awayE, p = awayP,
          gf = awayGf, gc = awayGc, dg = awayGf - awayGc, pts = awayPts
        )
      }

      // Sort and assign positions
      val sortedTeams = teams.values.sortedWith(
        compareByDescending<Team> { it.pts }
          .thenByDescending { it.dg }
          .thenByDescending { it.gf }
          .thenBy { it.name }
      ).mapIndexed { index, team -> team.copy(position = index + 1) }

      val batch = db.batch()
      sortedTeams.forEach { team ->
        val doc = teamsRef.document(team.id)
        val createdAt = teamsSnapshot.documents.find { it.id == team.id }?.getTimestamp("createdAt")
          ?: FieldValue.serverTimestamp()
        batch.set(
          doc,
          mapOf(
            "name" to team.name,
            "shortName" to team.shortName,
            "shieldColor" to team.shieldColor,
            "accentColor" to team.accentColor,
            "badgeIcon" to team.badgeIcon,
            "position" to team.position,
            "pj" to team.pj,
            "g" to team.g,
            "e" to team.e,
            "p" to team.p,
            "gf" to team.gf,
            "gc" to team.gc,
            "dg" to team.dg,
            "pts" to team.pts,
            "createdAt" to createdAt,
            "updatedAt" to FieldValue.serverTimestamp()
          )
        )
      }
      batch.commit().await()
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, teamsRef.path)
      throw e
    }
  }

  suspend fun resetTournament(userId: String) {
    val currentUid = requireUserId()
    if (currentUid != userId) throw IllegalStateException("Access denied")
    val teamsRef = db.collection("users").document(userId).collection("teams")
    val matchesRef = db.collection("users").document(userId).collection("matches")
    try {
      val existingTeams = teamsRef.get().await()
      val existingMatches = matchesRef.get().await()
      val deleteBatch = db.batch()
      existingTeams.forEach { deleteBatch.delete(it.reference) }
      existingMatches.forEach { deleteBatch.delete(it.reference) }
      deleteBatch.commit().await()

      // Reseed defaults
      seedInitialDataIfEmpty(userId)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.WRITE, teamsRef.path)
      throw e
    }
  }
}
