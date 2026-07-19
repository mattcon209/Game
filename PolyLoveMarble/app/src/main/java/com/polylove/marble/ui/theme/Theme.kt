package com.polylove.marble.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkBDSMColorScheme = darkColorScheme(
    primary = LatexCrimson,
    secondary = SeductiveViolet,
    tertiary = BrassGold,
    background = ObsidianBlack,
    surface = LeatherDarkPurple,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = SilkPurple,
    onSurface = SilkPurple
)

@Composable
fun PolyLoveMarbleTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkBDSMColorScheme,
        content = content
    )
}
