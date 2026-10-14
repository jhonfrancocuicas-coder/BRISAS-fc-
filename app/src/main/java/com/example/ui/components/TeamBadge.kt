package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Team

fun parseColor(hex: String, fallback: Color = Color(0xFF14532D)): Color {
  return try {
    Color(android.graphics.Color.parseColor(hex))
  } catch (e: Exception) {
    fallback
  }
}

@Composable
fun TeamBadge(
  team: Team,
  modifier: Modifier = Modifier,
  size: Dp = 38.dp
) {
  val primaryColor = parseColor(team.shieldColor)
  val accentColor = parseColor(team.accentColor, Color.White)

  Box(
    modifier = modifier
      .size(size)
      .clip(RoundedCornerShape(8.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(primaryColor, primaryColor.copy(alpha = 0.8f))
        )
      )
      .border(1.5.dp, accentColor.copy(alpha = 0.9f), RoundedCornerShape(8.dp)),
    contentAlignment = Alignment.Center
  ) {
    val iconVector = when (team.badgeIcon) {
      "wolf" -> Icons.Default.MilitaryTech
      "alligator" -> Icons.Default.Shield
      "circle_shield" -> Icons.Default.SportsSoccer
      "star_shield" -> Icons.Default.Star
      "castle_shield" -> Icons.Default.Shield
      "crown_shield" -> Icons.Default.EmojiEvents
      "beast_shield" -> Icons.Default.AutoAwesome
      "falcon_shield" -> Icons.Default.MilitaryTech
      "lion_shield" -> Icons.Default.Shield
      "badge_shield" -> Icons.Default.ElectricBolt
      "star_circle" -> Icons.Default.Star
      "wings_shield" -> Icons.Default.Visibility
      else -> Icons.Default.Shield
    }

    Icon(
      imageVector = iconVector,
      contentDescription = "${team.name} badge",
      tint = accentColor,
      modifier = Modifier.size(size * 0.58f)
    )
  }
}

@Composable
fun LeagueHeaderBadge(
  modifier: Modifier = Modifier,
  size: Dp = 68.dp
) {
  Box(
    modifier = modifier
      .size(size)
      .clip(RoundedCornerShape(12.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(Color(0xFF15803D), Color(0xFF0F3D20))
        )
      )
      .border(2.dp, Color(0xFFEAB308), RoundedCornerShape(12.dp)),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .size(size * 0.75f)
        .clip(CircleShape)
        .background(Color(0x33FFFFFF)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.SportsSoccer,
        contentDescription = "Liga Fronteo Obelisco",
        tint = Color(0xFFFDE047),
        modifier = Modifier.size(size * 0.5f)
      )
    }
  }
}
