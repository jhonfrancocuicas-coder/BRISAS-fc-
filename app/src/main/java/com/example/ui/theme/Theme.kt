package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = Green80,
  onPrimary = FronteoGreenDark,
  primaryContainer = FronteoGreenHeader,
  onPrimaryContainer = Color(0xFFD1FAE5),
  secondary = Gold80,
  onSecondary = Color(0xFF422006),
  surface = Color(0xFF141915),
  onSurface = Color(0xFFE5E7EB),
  background = Color(0xFF0F1410),
  onBackground = Color(0xFFE5E7EB)
)

private val LightColorScheme = lightColorScheme(
  primary = FronteoGreenDark,
  onPrimary = Color.White,
  primaryContainer = FronteoGreenHeader,
  onPrimaryContainer = Color.White,
  secondary = FronteoGold,
  onSecondary = Color(0xFF422006),
  surface = FronteoCream,
  onSurface = FronteoDark,
  background = FronteoCream,
  onBackground = FronteoDark
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep branded tournament theme
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
