package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Team

@Composable
fun TeamDetailDialog(
  team: Team,
  onDismiss: () -> Unit
) {
  val teamColor = parseColor(team.shieldColor)

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        TeamBadge(team = team, size = 44.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = team.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = teamColor
          )
          Text(
            text = "Posición actual: #${team.position}",
            fontSize = 12.sp,
            color = Color(0xFF6B7280)
          )
        }
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth()
      ) {
        HorizontalDivider(color = Color(0x1A1B4D3E), thickness = 1.dp)
        Spacer(modifier = Modifier.height(14.dp))

        // Large Points & Goal Diff Highlight
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          StatCard(title = "Puntos", value = "${team.pts}", highlight = true)
          StatCard(title = "Dif. Goles", value = if (team.dg > 0) "+${team.dg}" else "${team.dg}")
          StatCard(title = "Jugados", value = "${team.pj}")
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Breakdown stats grid
        Surface(
          color = Color(0xFFF3ECE0),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            StatRow(label = "Partidos Ganados (G)", value = "${team.g}")
            StatRow(label = "Partidos Empatados (E)", value = "${team.e}")
            StatRow(label = "Partidos Perdidos (P)", value = "${team.p}")
            HorizontalDivider(color = Color(0x1A000000), modifier = Modifier.padding(vertical = 4.dp))
            StatRow(label = "Goles a Favor (GF)", value = "${team.gf}")
            StatRow(label = "Goles en Contra (GC)", value = "${team.gc}")
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D))
      ) {
        Text("Cerrar")
      }
    }
  )
}

@Composable
private fun StatCard(
  title: String,
  value: String,
  highlight: Boolean = false
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (highlight) Color(0xFFDCFCE7) else Color(0xFFF3ECE0))
      .padding(horizontal = 14.dp, vertical = 8.dp)
  ) {
    Text(
      text = value,
      fontSize = 20.sp,
      fontWeight = FontWeight.Black,
      color = if (highlight) Color(0xFF15803D) else Color(0xFF1F2937)
    )
    Text(
      text = title,
      fontSize = 10.sp,
      fontWeight = FontWeight.Bold,
      color = Color(0xFF6B7280)
    )
  }
}

@Composable
private fun StatRow(
  label: String,
  value: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(text = label, fontSize = 12.sp, color = Color(0xFF4B5563))
    Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
  }
}
