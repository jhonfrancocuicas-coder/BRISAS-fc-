package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Match
import com.example.data.model.Team

@Composable
fun FixturesView(
  matches: List<Match>,
  teams: List<Team>,
  onMatchClick: (Match) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedRound by remember { mutableIntStateOf(1) }

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
    // Header
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(bottom = 8.dp)
    ) {
      Icon(
        imageVector = Icons.Default.CalendarToday,
        contentDescription = null,
        tint = Color(0xFF15803D),
        modifier = Modifier.size(22.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Calendario de Partidos",
        fontSize = 20.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFF1B4D3E)
      )
    }

    Text(
      text = "Liga Fronteo Obelisco 2026 - Clasificación y Resultados",
      fontSize = 12.sp,
      color = Color(0xFF78716C),
      modifier = Modifier.padding(bottom = 12.dp)
    )

    // Round Selector Chips
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 14.dp)
    ) {
      items(rounds) { roundNum ->
        FilterChip(
          selected = selectedRound == roundNum,
          onClick = { selectedRound = roundNum },
          label = { Text("Jornada $roundNum", fontWeight = FontWeight.Bold) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF15803D),
            selectedLabelColor = Color.White
          )
        )
      }
    }

    // Match Cards
    Column(
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      roundMatches.forEach { match ->
        val homeTeam = teams.find { it.id == match.homeTeamId }
        val awayTeam = teams.find { it.id == match.awayTeamId }

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onMatchClick(match) },
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F1)),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp)
          ) {
            // Match Date / Status pill
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = match.matchDate.ifBlank { "Fecha por definir" },
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B7280)
              )

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (match.status == "finished") Color(0xFFDCFCE7) else Color(0xFFF3F4F6)
              ) {
                Text(
                  text = if (match.status == "finished") "Finalizado" else "Programado",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (match.status == "finished") Color(0xFF15803D) else Color(0xFF4B5563),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Teams & Score
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Home Team
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
              ) {
                Text(
                  text = match.homeTeamName,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = homeTeam?.shieldColor?.let { parseColor(it) } ?: Color(0xFF1F2937),
                  textAlign = TextAlign.End,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.width(6.dp))
                homeTeam?.let {
                  TeamBadge(team = it, size = 26.dp)
                }
              }

              // Score or VS
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (match.status == "finished") Color(0xFF1B4D3E) else Color(0xFFE5E7EB),
                modifier = Modifier.padding(horizontal = 10.dp)
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
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
              ) {
                awayTeam?.let {
                  TeamBadge(team = it, size = 26.dp)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = match.awayTeamName,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = awayTeam?.shieldColor?.let { parseColor(it) } ?: Color(0xFF1F2937),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  modifier = Modifier.weight(1f, fill = false)
                )
              }
            }
          }
        }
      }
    }
  }
}
