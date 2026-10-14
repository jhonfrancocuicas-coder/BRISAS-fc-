package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Match
import com.example.data.model.Team
import com.google.firebase.auth.FirebaseUser

@Composable
fun AdminPanel(
  currentUser: FirebaseUser,
  teams: List<Team>,
  matches: List<Match>,
  onUpdateMatchScore: (Match, Int, Int, Boolean) -> Unit,
  onRecalculateStandings: () -> Unit,
  onResetTournament: () -> Unit,
  onSignOut: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedRound by remember { mutableIntStateOf(1) }
  var matchBeingEdited by remember { mutableStateOf<Match?>(null) }
  var showResetConfirmation by remember { mutableStateOf(false) }

  val rounds = remember(matches) {
    matches.map { it.round }.distinct().sorted().ifEmpty { listOf(1, 2) }
  }

  val roundMatches = remember(matches, selectedRound) {
    matches.filter { it.round == selectedRound }
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(14.dp)
  ) {
    // Admin Session Header Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF1B4D3E)),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFF14532D)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AdminPanelSettings,
            contentDescription = null,
            tint = Color(0xFFFDE047),
            modifier = Modifier.size(26.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Administrador Activo",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFFDE047)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.CloudDone,
              contentDescription = "Synced",
              tint = Color(0xFF4ADE80),
              modifier = Modifier.size(15.dp)
            )
          }
          Text(
            text = currentUser.displayName ?: currentUser.email?.substringBefore("@") ?: "Admin",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = currentUser.email ?: "",
            fontSize = 11.sp,
            color = Color(0xFFD1D5DB)
          )
        }

        IconButton(
          onClick = onSignOut,
          modifier = Modifier.testTag("admin_sign_out_button")
        ) {
          Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = "Cerrar sesión",
            tint = Color(0xFFF87171)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Quick Actions Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      OutlinedButton(
        onClick = onRecalculateStandings,
        modifier = Modifier
          .weight(1f)
          .testTag("recalculate_standings_button"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = Color(0xFF15803D)
        )
      ) {
        Icon(
          imageVector = Icons.Default.Refresh,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text("Recalcular", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }

      OutlinedButton(
        onClick = { showResetConfirmation = true },
        modifier = Modifier
          .weight(1f)
          .testTag("reset_tournament_button"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = Color(0xFFB91C1C)
        )
      ) {
        Icon(
          imageVector = Icons.Default.RestartAlt,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text("Reiniciar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Section Title
    Text(
      text = "Gestión de Partidos y Marcadores",
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold,
      color = Color(0xFF1B4D3E)
    )
    Text(
      text = "Selecciona una jornada para registrar o modificar los resultados",
      fontSize = 12.sp,
      color = Color(0xFF6B7280)
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Round Selector Chips
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(rounds) { roundNum ->
        FilterChip(
          selected = selectedRound == roundNum,
          onClick = { selectedRound = roundNum },
          label = { Text("Jornada $roundNum", fontSize = 12.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF15803D),
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Matches List in Round
    if (roundMatches.isEmpty()) {
      Surface(
        color = Color(0xFFF3ECE0),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "No hay partidos registrados en esta jornada.",
          fontSize = 13.sp,
          color = Color(0xFF78716C),
          modifier = Modifier.padding(16.dp),
          textAlign = TextAlign.Center
        )
      }
    } else {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        roundMatches.forEach { match ->
          val homeTeam = teams.find { it.id == match.homeTeamId }
          val awayTeam = teams.find { it.id == match.awayTeamId }

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { matchBeingEdited = match },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F1)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Home Team
              Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
              ) {
                Text(
                  text = match.homeTeamName,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = homeTeam?.shieldColor?.let { parseColor(it) } ?: Color(0xFF1F2937),
                  textAlign = TextAlign.End
                )
              }

              // Score Box
              Surface(
                color = if (match.status == "finished") Color(0xFF1B4D3E) else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.padding(horizontal = 12.dp)
              ) {
                Text(
                  text = if (match.status == "finished") "${match.homeScore} - ${match.awayScore}" else "VS",
                  fontWeight = FontWeight.Black,
                  fontSize = 14.sp,
                  color = if (match.status == "finished") Color.White else Color(0xFF374151),
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
              }

              // Away Team
              Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
              ) {
                Text(
                  text = match.awayTeamName,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = awayTeam?.shieldColor?.let { parseColor(it) } ?: Color(0xFF1F2937)
                )
              }

              IconButton(
                onClick = { matchBeingEdited = match },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Edit,
                  contentDescription = "Editar marcador",
                  tint = Color(0xFF15803D),
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }
  }

  // Edit Match Score Dialog
  matchBeingEdited?.let { match ->
    var homeScore by remember { mutableIntStateOf(match.homeScore) }
    var awayScore by remember { mutableIntStateOf(match.awayScore) }
    var isFinished by remember { mutableStateOf(match.status == "finished") }

    AlertDialog(
      onDismissRequest = { matchBeingEdited = null },
      title = {
        Text(
          text = "Registrar Marcador - Jornada ${match.round}",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1B4D3E)
        )
      },
      text = {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "${match.homeTeamName} vs ${match.awayTeamName}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF374151),
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(18.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Home score stepper
            ScoreStepper(
              teamName = match.homeTeamName,
              score = homeScore,
              onIncrement = { homeScore++ },
              onDecrement = { if (homeScore > 0) homeScore-- }
            )

            Text(":", fontSize = 24.sp, fontWeight = FontWeight.Black)

            // Away score stepper
            ScoreStepper(
              teamName = match.awayTeamName,
              score = awayScore,
              onIncrement = { awayScore++ },
              onDecrement = { if (awayScore > 0) awayScore-- }
            )
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Match Finished Checkbox/Toggle
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(if (isFinished) Color(0xFFDCFCE7) else Color(0xFFF3F4F6))
              .clickable { isFinished = !isFinished }
              .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = if (isFinished) Icons.Default.Check else Icons.Default.SportsSoccer,
              contentDescription = null,
              tint = if (isFinished) Color(0xFF15803D) else Color(0xFF6B7280),
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isFinished) "Partido Finalizado (Actualiza Tabla)" else "Partido No Finalizado",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = if (isFinished) Color(0xFF15803D) else Color(0xFF4B5563)
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onUpdateMatchScore(match, homeScore, awayScore, isFinished)
            matchBeingEdited = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D))
        ) {
          Text("Guardar Marcador")
        }
      },
      dismissButton = {
        TextButton(onClick = { matchBeingEdited = null }) {
          Text("Cancelar")
        }
      }
    )
  }

  // Reset Confirmation Dialog
  if (showResetConfirmation) {
    AlertDialog(
      onDismissRequest = { showResetConfirmation = false },
      title = { Text("¿Reiniciar Torneo?") },
      text = {
        Text("Esta acción reiniciará todos los marcadores y restablecerá la tabla de posiciones a 0 puntos para los 12 equipos.")
      },
      confirmButton = {
        Button(
          onClick = {
            onResetTournament()
            showResetConfirmation = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB91C1C))
        ) {
          Text("Sí, Reiniciar")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetConfirmation = false }) {
          Text("Cancelar")
        }
      }
    )
  }
}

@Composable
private fun ScoreStepper(
  teamName: String,
  score: Int,
  onIncrement: () -> Unit,
  onDecrement: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = teamName,
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      color = Color(0xFF4B5563),
      maxLines = 1
    )
    Spacer(modifier = Modifier.height(6.dp))
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onDecrement,
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(Color(0xFFE5E7EB))
      ) {
        Icon(Icons.Default.Remove, contentDescription = "Menos", modifier = Modifier.size(16.dp))
      }

      Text(
        text = "$score",
        fontSize = 22.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFF14532D),
        modifier = Modifier.padding(horizontal = 14.dp)
      )

      IconButton(
        onClick = onIncrement,
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(Color(0xFF15803D))
      ) {
        Icon(Icons.Default.Add, contentDescription = "Más", tint = Color.White, modifier = Modifier.size(16.dp))
      }
    }
  }
}
