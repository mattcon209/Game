package com.polylove.marble.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkFantasyColorScheme = darkColorScheme(
    primary = CrimsonGlow,
    secondary = PurpleArcane,
    tertiary = GoldPrimary,
    background = ObsidianBlack,
    surface = DarkCardBg,
    surfaceVariant = DarkElevated,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextMuted,
    outline = CrimsonPrimary,
    outlineVariant = Color(0xFF332244)
)

@Composable
fun PolyLoveMarbleTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkFantasyColorScheme,
        content = content
    )
}
