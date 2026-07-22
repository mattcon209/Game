package com.polylove.marble.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================
// DARK FANTASY UI KIT — Color System
// Matches the dark_fantasy_ui_kit.html design specification
// ============================================================

// Core Backgrounds
val ObsidianBlack = Color(0xFF07060A)
val DarkCardBg = Color(0xFF120E1A)
val DarkSurface = Color(0xFF0D0A14)
val DarkElevated = Color(0xFF140F1E)

// Primary Accents (Crimson Theme)
val CrimsonPrimary = Color(0xFF800016)
val CrimsonGlow = Color(0xFFff2a4b)
val CrimsonDeep = Color(0xFF3C000A)

// Gold Accents
val GoldPrimary = Color(0xFFd4af37)
val GoldBright = Color(0xFFffe270)
val GoldDark = Color(0xFF8a6d1c)

// Cyan Accents
val CyanGlow = Color(0xFF00d2ff)

// Purple Accents
val PurpleArcane = Color(0xFFa855f7)

// Text Colors
val TextPrimary = Color(0xFFf0ebf5)
val TextMuted = Color(0xFF9e95a8)
val TextGold = Color(0xFFd4af37)
val TextCrimson = Color(0xFFff2a4b)

// Other colors
val WhipBrown = Color(0xFF4E2F1D)

// Gemstone Colors (ROYGBIV + Black & White)
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

fun Color.toHex(): String = String.format("#%08X", this.value.toLong())
