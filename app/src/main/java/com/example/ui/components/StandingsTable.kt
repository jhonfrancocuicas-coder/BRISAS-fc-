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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InitialData
import com.example.data.model.Team

@Composable
fun StandingsHeader(
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    LeagueHeaderBadge(size = 58.dp)
    Spacer(modifier = Modifier.width(14.dp))
    Column(
      modifier = Modifier.weight(1f)
    ) {
      Text(
        text = "TABLA DE",
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.sp,
        color = Color(0xFF1B4D3E),
        lineHeight = 26.sp
      )
      Text(
        text = "POSICIONES",
        fontSize = 28.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 2.sp,
        color = Color(0xFF1B4D3E),
        lineHeight = 30.sp
      )
    }
  }
}

@Composable
fun TableColumnsHeader(
  showDg: Boolean = false,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
      .background(Color(0xFF1B4D3E))
      .padding(vertical = 10.dp, horizontal = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "#",
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      color = Color.White,
      textAlign = TextAlign.Center,
      modifier = Modifier.width(26.dp)
    )
    Text(
      text = "EQUIPO",
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      color = Color.White,
      modifier = Modifier
        .weight(1f)
        .padding(start = 6.dp)
    )
    HeaderStatCell(text = "PJ")
    HeaderStatCell(text = "G")
    HeaderStatCell(text = "E")
    HeaderStatCell(text = "P")
    HeaderStatCell(text = "GF")
    HeaderStatCell(text = "GC")
    if (showDg) {
      HeaderStatCell(text = "DG")
    }
    HeaderStatCell(text = "PTS", isHighlight = true)
  }
}

@Composable
private fun HeaderStatCell(
  text: String,
  isHighlight: Boolean = false
) {
  Text(
    text = text,
    fontWeight = if (isHighlight) FontWeight.Black else FontWeight.Bold,
    fontSize = 12.sp,
    color = if (isHighlight) Color(0xFFFDE047) else Color.White,
    textAlign = TextAlign.Center,
    modifier = Modifier.width(26.dp)
  )
}

@Composable
fun StandingsTableRow(
  team: Team,
  position: Int,
  isEven: Boolean,
  showDg: Boolean = false,
  onTeamClick: (Team) -> Unit,
  modifier: Modifier = Modifier
) {
  val rowBg = if (isEven) Color(0xFFFBF8F1) else Color(0xFFF3ECE0)
  val teamTextColor = parseColor(team.shieldColor, Color(0xFF1B4D3E))

  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(rowBg)
      .clickable { onTeamClick(team) }
      .testTag("team_row_${team.id}")
      .padding(vertical = 9.dp, horizontal = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Position
    Text(
      text = "$position",
      fontWeight = FontWeight.SemiBold,
      fontSize = 14.sp,
      color = Color(0xFF4B5563),
      textAlign = TextAlign.Center,
      modifier = Modifier.width(26.dp)
    )

    // Team Badge + Name
    TeamBadge(
      team = team,
      size = 28.dp,
      modifier = Modifier.padding(start = 2.dp, end = 6.dp)
    )

    Text(
      text = team.name,
      fontWeight = FontWeight.Black,
      fontSize = 13.sp,
      letterSpacing = 0.5.sp,
      color = teamTextColor,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.weight(1f)
    )

    // Stats
    StatValueCell(value = team.pj)
    StatValueCell(value = team.g)
    StatValueCell(value = team.e)
    StatValueCell(value = team.p)
    StatValueCell(value = team.gf)
    StatValueCell(value = team.gc)
    if (showDg) {
      StatValueCell(value = team.dg)
    }
    StatValueCell(value = team.pts, isBold = true)
  }
}

@Composable
private fun StatValueCell(
  value: Int,
  isBold: Boolean = false
) {
  Text(
    text = "$value",
    fontWeight = if (isBold) FontWeight.Black else FontWeight.SemiBold,
    fontSize = if (isBold) 14.sp else 13.sp,
    color = if (isBold) Color(0xFF14532D) else Color(0xFF1F2937),
    textAlign = TextAlign.Center,
    modifier = Modifier.width(26.dp)
  )
}

@Composable
fun StandingsTable(
  teams: List<Team>,
  onTeamClick: (Team) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp),
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F1))
  ) {
    Column {
      StandingsHeader()
      TableColumnsHeader()

      teams.forEachIndexed { index, team ->
        StandingsTableRow(
          team = team,
          position = index + 1,
          isEven = index % 2 == 0,
          onTeamClick = onTeamClick
        )
        if (index < teams.size - 1) {
          HorizontalDivider(
            color = Color(0x1A1B4D3E),
            thickness = 0.8.dp
          )
        }
      }

      // Table Footer
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFFEDE5D5))
          .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "Liga Inicial 2026",
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = Color(0xFF57534E)
        )
        Text(
          text = "Liga Fronteo Obelisco - Temporada 2026",
          fontSize = 11.sp,
          color = Color(0xFF78716C)
        )
      }
    }
  }
}
