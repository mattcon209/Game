package com.polylove.marble.ui.theme

import androidx.compose.ui.graphics.Color

// Deep Latex & Leather Obsidian Theme
val ObsidianBlack = Color(0xFF07040B)
val LeatherDarkPurple = Color(0xFF140D1E)
val LatexCrimson = Color(0xFF8B0000)
val IntensityRed = Color(0xFFD32F2F)
val SteelGrey = Color(0xFF7A7A8A)
val BrassGold = Color(0xFFC5A059)
val SeductiveViolet = Color(0xFF5C1D8D)
val SilkPurple = Color(0xFFD28EFF)
val WhipBrown = Color(0xFF4E2F1D)

// EXEXCLUSIVE REQUESTED: ROYGBIV + Black & White Gemstone Colors
data class GemstoneColor(
    val name: String,
    val color: Color,
    val description: String
)

val GemstoneColors = listOf(
    GemstoneColor("Ruby", Color(0xFFE50914), "High-gloss blood red"),
    GemstoneColor("Amber", Color(0xFFFF7F00), "High-gloss glowing orange"),
    GemstoneColor("Sunstone", Color(0xFFFFD700), "High-gloss solar yellow"),
    GemstoneColor("Emerald", Color(0xFF00D26A), "High-gloss velvet green"),
    GemstoneColor("Sapphire", Color(0xFF0070FF), "High-gloss deep sapphire blue"),
    GemstoneColor("Amethyst", Color(0xFF8A2BE2), "High-gloss mystical purple"),
    GemstoneColor("Rose", Color(0xFFFF007F), "High-gloss hot magenta pink"),
    GemstoneColor("Obsidian", Color(0xFF222222), "High-gloss polished black"),
    GemstoneColor("Silk", Color(0xFFF5F5F5), "High-gloss pearlescent silk white")
)

// Helper to convert Color to Hex string
fun Color.toHex(): String = String.format("#%08X", this.value.toLong())
