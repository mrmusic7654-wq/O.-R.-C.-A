package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AbyssalColorScheme = darkColorScheme(
    primary = NeonRed,
    secondary = CyanIntel,
    tertiary = NeonRedPulse,
    background = VantaBlack,
    surface = AbyssBlack,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = GlassBackground,
    onSurfaceVariant = TextSecondary,
    error = ErrorColor
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  val view = LocalView.current
  if (!view.isInEditMode) {
      SideEffect {
          val window = (view.context as Activity).window
          window.statusBarColor = VantaBlack.toArgb()
          window.navigationBarColor = VantaBlack.toArgb()
          WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
      }
  }

  MaterialTheme(colorScheme = AbyssalColorScheme, typography = Typography, content = content)
}
