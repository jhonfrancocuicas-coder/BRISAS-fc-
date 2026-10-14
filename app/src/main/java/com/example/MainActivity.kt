package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.repository.TournamentRepository
import com.example.ui.components.AdminPanel
import com.example.ui.components.AdminSignInCard
import com.example.ui.components.FixturesView
import com.example.ui.components.StandingsTable
import com.example.ui.components.TeamDetailDialog
import com.example.ui.components.TeamsView
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.TournamentTab
import com.example.ui.viewmodel.TournamentViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        TournamentApp()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentApp() {
  val context = LocalContext.current
  val credentialManager = remember { CredentialManager.create(context) }
  val repository = remember { TournamentRepository(context) }
  val viewModel: TournamentViewModel = viewModel { TournamentViewModel(repository) }

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(uiState.userMessage) {
    uiState.userMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearMessage()
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Liga Fronteo Obelisco",
              fontWeight = FontWeight.Black,
              fontSize = 18.sp,
              color = Color(0xFFFDE047)
            )
          }
        },
        actions = {
          if (uiState.currentUser != null) {
            Box(
              modifier = Modifier
                .padding(end = 12.dp)
                .clip(CircleShape)
                .background(Color(0xFF22C55E))
                .size(10.dp)
            )
          }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = Color(0xFF1B4D3E),
          titleContentColor = Color.White
        )
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = Color(0xFF14532D),
        contentColor = Color.White
      ) {
        NavigationBarItem(
          selected = uiState.currentTab == TournamentTab.STANDINGS,
          onClick = { viewModel.selectTab(TournamentTab.STANDINGS) },
          icon = {
            Icon(
              imageVector = Icons.Default.FormatListNumbered,
              contentDescription = "Tabla de Posiciones"
            )
          },
          label = { Text("Tabla", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF14532D),
            selectedTextColor = Color(0xFFFDE047),
            indicatorColor = Color(0xFFFDE047),
            unselectedIconColor = Color(0xFFE5E7EB),
            unselectedTextColor = Color(0xFFD1D5DB)
          ),
          modifier = Modifier.testTag("nav_standings")
        )

        NavigationBarItem(
          selected = uiState.currentTab == TournamentTab.FIXTURES,
          onClick = { viewModel.selectTab(TournamentTab.FIXTURES) },
          icon = {
            Icon(
              imageVector = Icons.Default.CalendarMonth,
              contentDescription = "Jornadas"
            )
          },
          label = { Text("Jornadas", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF14532D),
            selectedTextColor = Color(0xFFFDE047),
            indicatorColor = Color(0xFFFDE047),
            unselectedIconColor = Color(0xFFE5E7EB),
            unselectedTextColor = Color(0xFFD1D5DB)
          ),
          modifier = Modifier.testTag("nav_fixtures")
        )

        NavigationBarItem(
          selected = uiState.currentTab == TournamentTab.TEAMS,
          onClick = { viewModel.selectTab(TournamentTab.TEAMS) },
          icon = {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = "Equipos"
            )
          },
          label = { Text("Equipos", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF14532D),
            selectedTextColor = Color(0xFFFDE047),
            indicatorColor = Color(0xFFFDE047),
            unselectedIconColor = Color(0xFFE5E7EB),
            unselectedTextColor = Color(0xFFD1D5DB)
          ),
          modifier = Modifier.testTag("nav_teams")
        )

        NavigationBarItem(
          selected = uiState.currentTab == TournamentTab.ADMIN,
          onClick = { viewModel.selectTab(TournamentTab.ADMIN) },
          icon = {
            BadgedBox(badge = {
              if (uiState.currentUser != null) {
                Badge(containerColor = Color(0xFF22C55E))
              }
            }) {
              Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = "Panel de Admin"
              )
            }
          },
          label = { Text("Admin", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF14532D),
            selectedTextColor = Color(0xFFFDE047),
            indicatorColor = Color(0xFFFDE047),
            unselectedIconColor = Color(0xFFE5E7EB),
            unselectedTextColor = Color(0xFFD1D5DB)
          ),
          modifier = Modifier.testTag("nav_admin")
        )
      }
    },
    snackbarHost = { SnackbarHost(snackbarHostState) }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFEDE5D5))
    ) {
      item {
        when (uiState.currentTab) {
          TournamentTab.STANDINGS -> {
            StandingsTable(
              teams = uiState.teams,
              onTeamClick = { team -> viewModel.showTeamDetail(team) }
            )
          }

          TournamentTab.FIXTURES -> {
            FixturesView(
              matches = uiState.matches,
              teams = uiState.teams,
              onMatchClick = { match ->
                if (uiState.currentUser != null) {
                  viewModel.selectTab(TournamentTab.ADMIN)
                }
              }
            )
          }

          TournamentTab.TEAMS -> {
            TeamsView(
              teams = uiState.teams,
              onTeamClick = { team -> viewModel.showTeamDetail(team) }
            )
          }

          TournamentTab.ADMIN -> {
            val user = uiState.currentUser
            if (user == null) {
              AdminSignInCard(
                onSignInSuccess = {
                  // User state is observed automatically by ViewModel
                }
              )
            } else {
              AdminPanel(
                currentUser = user,
                teams = uiState.teams,
                matches = uiState.matches,
                onUpdateMatchScore = { match, home, away, finished ->
                  viewModel.updateMatchScore(match, home, away, finished)
                },
                onRecalculateStandings = { viewModel.recalculateStandings() },
                onResetTournament = { viewModel.resetTournament() },
                onSignOut = { viewModel.signOut(context, credentialManager) }
              )
            }
          }
        }
      }
    }
  }

  // Team Detail Modal
  uiState.selectedTeamDetail?.let { team ->
    TeamDetailDialog(
      team = team,
      onDismiss = { viewModel.showTeamDetail(null) }
    )
  }
}
