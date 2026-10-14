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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Team

@Composable
fun TeamsView(
  teams: List<Team>,
  onTeamClick: (Team) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(14.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(bottom = 6.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Shield,
        contentDescription = null,
        tint = Color(0xFF15803D),
        modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Equipos Clasificados (12)",
        fontSize = 20.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFF1B4D3E)
      )
    }

    Text(
      text = "Liga Fronteo Obelisco - Temporada 2026",
      fontSize = 12.sp,
      color = Color(0xFF78716C),
      modifier = Modifier.padding(bottom = 14.dp)
    )

    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      teams.forEach { team ->
        val teamColor = parseColor(team.shieldColor)

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onTeamClick(team) },
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F1)),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Position Badge
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFEDE5D5)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${team.position}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF44403C)
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Badge
            TeamBadge(team = team, size = 36.dp)

            Spacer(modifier = Modifier.width(12.dp))

            // Name and stats
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = team.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = teamColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "PJ: ${team.pj}  |  G: ${team.g}  E: ${team.e}  P: ${team.p}  |  GF: ${team.gf}  GC: ${team.gc}",
                fontSize = 11.sp,
                color = Color(0xFF6B7280)
              )
            }

            // Points Pill
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFF15803D)
            ) {
              Text(
                text = "${team.pts} PTS",
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }
          }
        }
      }
    }
  }
}
