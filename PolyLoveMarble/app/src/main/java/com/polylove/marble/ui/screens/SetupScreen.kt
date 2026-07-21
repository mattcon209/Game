package com.polylove.marble.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.game.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.polylove.marble.R
import com.polylove.marble.ui.ChainsOfDesireLogo
import com.polylove.marble.ui.GameScreen
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.components.*
import com.polylove.marble.ui.theme.*

// ============================================================
// DARK FANTASY UI KIT - OCTAGONAL BUTTON
// Matches btn-1 from dark_fantasy_ui_kit.svg
// ============================================================
@Composable
fun OctagonalFantasyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    enabled: Boolean = true
) {
    val haptic = LocalHapticFeedback.current
    val borderColor = if (isPrimary) Color(0xFFD4AF37) else Color.White
    val fillColor = if (isPrimary) Color(0xFF1D0D24) else Color(0xFF09080E)
    val textColor = if (isPrimary) Color(0xFFD4AF37) else Color.White

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(enabled = enabled) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cut = 12f * density

            // Outer octagonal path
            val outerPath = Path().apply {
                moveTo(cut, 0f)
                lineTo(w - cut, 0f)
                lineTo(w, cut)
                lineTo(w, h - cut)
                lineTo(w - cut, h)
                lineTo(cut, h)
                lineTo(0f, h - cut)
                lineTo(0f, cut)
                close()
            }

            // Fill
            drawPath(outerPath, color = fillColor.copy(alpha = 0.85f))

            // Outer border
            drawPath(outerPath, color = borderColor, style = Stroke(width = 2f * density))

            // Inner dashed border
            val innerPath = Path().apply {
                val inset = 4f * density
                moveTo(cut + inset, inset)
                lineTo(w - cut - inset, inset)
                lineTo(w - inset, cut + inset)
                lineTo(w - inset, h - cut - inset)
                lineTo(w - cut - inset, h - inset)
                lineTo(cut + inset, h - inset)
                lineTo(inset, h - cut - inset)
                lineTo(inset, cut + inset)
                close()
            }
            drawPath(innerPath, color = borderColor.copy(alpha = 0.5f), style = Stroke(
                width = 1f * density,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * density, 2f * density))
            ))

            // Arrow triangles on sides
            val triSize = 6f * density
            // Left triangle
            drawPath(Path().apply {
                moveTo(0f, h / 2f)
                lineTo(triSize, h / 2f - triSize)
                lineTo(triSize, h / 2f + triSize)
                close()
            }, color = borderColor)
            // Right triangle
            drawPath(Path().apply {
                moveTo(w, h / 2f)
                lineTo(w - triSize, h / 2f - triSize)
                lineTo(w - triSize, h / 2f + triSize)
                close()
            }, color = borderColor)
        }

        Text(
            text = text,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 1.sp,
            color = textColor
        )
    }
}

// ============================================================
// DARK FANTASY UI KIT - HEXAGONAL BUTTON
// Matches btn-2 from dark_fantasy_ui_kit.svg
// ============================================================
@Composable
fun HexagonalFantasyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    enabled: Boolean = true
) {
    val haptic = LocalHapticFeedback.current
    val borderColor = if (isPrimary) Color(0xFFD4AF37) else Color.White
    val fillColor = if (isPrimary) Color(0xFF1D0D24) else Color(0xFF09080E)
    val textColor = if (isPrimary) Color(0xFFD4AF37) else Color.White

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(enabled = enabled) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val r = h / 2f

            // Hexagonal pill path
            val hexPath = Path().apply {
                moveTo(r, 0f)
                lineTo(w - r, 0f)
                cubicTo(w, 0f, w, r, w, r)
                cubicTo(w, h - r, w, h, w - r, h)
                lineTo(r, h)
                cubicTo(0f, h, 0f, h - r, 0f, h - r)
                cubicTo(0f, r, 0f, 0f, r, 0f)
                close()
            }

            drawPath(hexPath, color = fillColor.copy(alpha = 0.85f))
            drawPath(hexPath, color = borderColor, style = Stroke(width = 2f * density))

            // Decorative dots on sides
            drawCircle(color = borderColor, radius = 3f * density, center = Offset(0f, h / 2f))
            drawCircle(color = borderColor, radius = 3f * density, center = Offset(w, h / 2f))
        }

        Text(
            text = text,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 1.sp,
            color = textColor
        )
    }
}

// ============================================================
// DARK FANTASY UI KIT - ORNATE DIVIDER
// Matches div-1 from dark_fantasy_ui_kit.svg
// ============================================================
@Composable
fun FantasyOrnateDivider(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
    ) {
        val w = size.width
        val h = size.height
        val cy = h / 2f
        val cx = w / 2f
        val density = this.density

        // Dual horizontal lines
        drawLine(
            color = Color.White.copy(alpha = 0.6f),
            start = Offset(0f, cy - 2f * density),
            end = Offset(w, cy - 2f * density),
            strokeWidth = 1.5f * density
        )
        drawLine(
            color = Color.White.copy(alpha = 0.6f),
            start = Offset(0f, cy + 2f * density),
            end = Offset(w, cy + 2f * density),
            strokeWidth = 1.5f * density
        )

        // Center diamond
        val dSize = 8f * density
        drawPath(
            path = Path().apply {
                moveTo(cx, cy - dSize)
                lineTo(cx + dSize, cy)
                moveTo(cx + dSize, cy)
                lineTo(cx, cy + dSize)
                moveTo(cx, cy + dSize)
                lineTo(cx - dSize, cy)
                moveTo(cx - dSize, cy)
                lineTo(cx, cy - dSize)
            },
            color = Color.White,
            style = Stroke(width = 1.5f * density)
        )

        // Wing triangles
        val wSize = 10f * density
        drawPath(
            path = Path().apply {
                moveTo(cx, cy)
                lineTo(cx - wSize, cy - wSize / 2f)
                lineTo(cx - wSize, cy + wSize / 2f)
                close()
            },
            color = Color.White
        )
        drawPath(
            path = Path().apply {
                moveTo(cx, cy)
                lineTo(cx + wSize, cy - wSize / 2f)
                lineTo(cx + wSize, cy + wSize / 2f)
                close()
            },
            color = Color.White
        )
    }
}

// ============================================================
// DARK FANTASY UI KIT - PROGRESS BAR (for player stats)
// Matches bar-1 from dark_fantasy_ui_kit.svg
// ============================================================
@Composable
fun FantasyProgressBar(
    label: String,
    current: Int,
    maximum: Int,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val fraction = if (maximum > 0) current.toFloat() / maximum.toFloat() else 0f

    Box(modifier = modifier.fillMaxWidth().height(28.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cut = 6f * density

            // Outer border path
            val borderPath = Path().apply {
                moveTo(cut, 0f)
                lineTo(w - cut, 0f)
                lineTo(w, cut)
                lineTo(w, h - cut)
                lineTo(w - cut, h)
                lineTo(cut, h)
                lineTo(0f, h - cut)
                lineTo(0f, cut)
                close()
            }

            // Dark background fill
            drawPath(borderPath, color = Color(0xFF09080E))

            // Border stroke
            drawPath(borderPath, color = Color.White, style = Stroke(width = 2f * density))

            // Progress fill
            val fillWidth = (w - 20f * density) * fraction
            if (fillWidth > 0f) {
                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(10f * density, 5f * density),
                    size = androidx.compose.ui.geometry.Size(fillWidth, h - 10f * density),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f * density)
                )
            }
        }

        // Label text
        Text(
            text = "$label: $current / $maximum",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

// ============================================================
// DARK FANTASY UI KIT - GOTHIC WINDOW FRAME
// Matches win-1 from dark_fantasy_ui_kit.svg
// ============================================================
@Composable
fun GothicWindowFrame(
    modifier: Modifier = Modifier,
    title: String = "",
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0D0A14))
            .border(2.5.dp, Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val density = this.density

            // Inner dashed border
            val inset = 4f * density
            drawRoundRect(
                color = Color.White.copy(alpha = 0.4f),
                topLeft = Offset(inset, inset),
                size = androidx.compose.ui.geometry.Size(w - 2 * inset, h - 2 * inset),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f * density),
                style = Stroke(
                    width = 1f * density,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * density, 2f * density))
                )
            )

            // Corner accents
            val cornerSize = 20f * density
            // Top-left
            drawLine(color = Color.White, start = Offset(0f, cornerSize), end = Offset(0f, 0f), strokeWidth = 2f * density)
            drawLine(color = Color.White, start = Offset(0f, 0f), end = Offset(cornerSize, 0f), strokeWidth = 2f * density)
            // Top-right
            drawLine(color = Color.White, start = Offset(w - cornerSize, 0f), end = Offset(w, 0f), strokeWidth = 2f * density)
            drawLine(color = Color.White, start = Offset(w, 0f), end = Offset(w, cornerSize), strokeWidth = 2f * density)
            // Bottom-left
            drawLine(color = Color.White, start = Offset(0f, h - cornerSize), end = Offset(0f, h), strokeWidth = 2f * density)
            drawLine(color = Color.White, start = Offset(0f, h), end = Offset(cornerSize, h), strokeWidth = 2f * density)
            // Bottom-right
            drawLine(color = Color.White, start = Offset(w - cornerSize, h), end = Offset(w, h), strokeWidth = 2f * density)
            drawLine(color = Color.White, start = Offset(w, h - cornerSize), end = Offset(w, h), strokeWidth = 2f * density)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFFD4AF37),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                FantasyOrnateDivider(modifier = Modifier.padding(bottom = 8.dp))
            }
            content()
        }
    }
}

// ============================================================
// DARK FANTASY UI KIT - MAGIC SIGIL (for spellbook selector)
// ============================================================
@Composable
fun MagicSigilFrame(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val sigilColor = if (isSelected) Color(0xFFD4AF37) else Color.White
    val bgColor = if (isSelected) Color(0xFF1D0D24) else Color(0xFF09080E)

    Box(
        modifier = modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(2.dp, sigilColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f
            val r = minOf(w, h) / 2f - 6f * density

            // Outer circle
            drawCircle(
                color = sigilColor.copy(alpha = 0.6f),
                radius = r,
                center = Offset(cx, cy),
                style = Stroke(width = 1.5f * density)
            )

            // Inner dashed circle
            drawCircle(
                color = sigilColor.copy(alpha = 0.3f),
                radius = r - 8f * density,
                center = Offset(cx, cy),
                style = Stroke(
                    width = 1f * density,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f * density, 3f * density))
                )
            )

            // Cardinal direction triangles
            val triSize = 4f * density
            // North
            drawPath(Path().apply {
                moveTo(cx, cy - r + 2f * density)
                lineTo(cx - triSize, cy - r + 8f * density)
                lineTo(cx + triSize, cy - r + 8f * density)
                close()
            }, color = sigilColor)
            // South
            drawPath(Path().apply {
                moveTo(cx, cy + r - 2f * density)
                lineTo(cx - triSize, cy + r - 8f * density)
                lineTo(cx + triSize, cy + r - 8f * density)
                close()
            }, color = sigilColor)
            // East
            drawPath(Path().apply {
                moveTo(cx + r - 2f * density, cy)
                lineTo(cx + r - 8f * density, cy - triSize)
                lineTo(cx + r - 8f * density, cy + triSize)
                close()
            }, color = sigilColor)
            // West
            drawPath(Path().apply {
                moveTo(cx - r + 2f * density, cy)
                lineTo(cx - r + 8f * density, cy - triSize)
                lineTo(cx - r + 8f * density, cy + triSize)
                close()
            }, color = sigilColor)
        }

        content()
    }
}

// ============================================================
// MAIN SETUP SCREEN - DARK FANTASY UI OVERHAUL
// ============================================================
@Composable
fun SetupScreen(viewModel: GameViewModel) {
    val hapticFeedback = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    // Tab State: LOBBY, SPELLBOOKS, MAP, SETTINGS
    var activeTab by remember { mutableStateOf("LOBBY") }

    SeductiveLeatherBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 1. HEADER BANNER
            Image(
                painter = painterResource(id = R.drawable.setup_header),
                contentDescription = "Chains of Desire Header Banner",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. DARK FANTASY TAB NAVIGATION
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color(0xFF09080E))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("LOBBY", "SPELLBOOKS", "MAP", "SETTINGS").forEach { tab ->
                    val isActive = activeTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                activeTab = tab
                            }
                            .background(
                                if (isActive) Color(0xFF1D0D24) else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = tab,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp,
                                letterSpacing = 1.5.sp,
                                color = if (isActive) Color(0xFFD4AF37) else Color.Gray
                            )
                            if (isActive) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.6f)
                                        .height(2.dp)
                                        .background(Color(0xFFD4AF37))
                                )
                            }
                        }
                    }
                }
            }

            // 3. CONTENT PANEL - GOTHIC WINDOW FRAME
            GothicWindowFrame(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)),
                title = when (activeTab) {
                    "LOBBY" -> "SESSION PARTNERS"
                    "SPELLBOOKS" -> "SPELL BOOKS"
                    "MAP" -> "MAP CONFIGURATION"
                    "SETTINGS" -> "GAME SETTINGS"
                    else -> ""
                }
            ) {
                when (activeTab) {
                    "LOBBY" -> LobbyTabContent(viewModel, hapticFeedback)
                    "SPELLBOOKS" -> SpellbooksTabContent(viewModel, hapticFeedback)
                    "MAP" -> MapTabContent(viewModel, hapticFeedback)
                    "SETTINGS" -> SettingsTabContent(viewModel, hapticFeedback)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. BOTTOM ACTION BUTTONS - DARK FANTASY STYLE

            // Spellbook Editor Button
            OctagonalFantasyButton(
                text = "SPELLBOOK EDITOR",
                isPrimary = false,
                onClick = {
                    viewModel.currentScreen = GameScreen.Editor
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Begin Session Button - Primary Octagonal
            OctagonalFantasyButton(
                text = "BEGIN SESSION",
                isPrimary = true,
                onClick = {
                    viewModel.board.clear()
                    viewModel.board.addAll(BoardCreator.createBoardForPacks(
                        viewModel.gridSize,
                        viewModel.selectedSpellbooks.toSet(),
                        viewModel.isBoardRandomized
                    ))
                    viewModel.currentPlayerIndex = 0
                    viewModel.currentScreen = GameScreen.Board
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ============================================================
// TAB CONTENT: LOBBY (Player Bars)
// ============================================================
@Composable
private fun LobbyTabContent(viewModel: GameViewModel, hapticFeedback: androidx.compose.ui.hapticfeedback.HapticFeedback) {
    val playerScrollState = rememberScrollState()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    ) {
        Text(
            text = "Session Partners (${viewModel.players.size}/8)",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Serif,
            color = Color(0xFFD4AF37)
        )
        IconButton(onClick = { viewModel.addPlayer() }, enabled = viewModel.players.size < 8) {
            Icon(
                Icons.Default.AddCircle,
                contentDescription = "Add Player",
                tint = if (viewModel.players.size < 8) Color(0xFFD4AF37) else Color.Gray,
                modifier = Modifier.size(26.dp)
            )
        }
    }

    FantasyOrnateDivider(modifier = Modifier.padding(bottom = 8.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .verticalScroll(playerScrollState),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.players.forEachIndexed { index, player ->
            Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                // Player bar with dark fantasy styling
                Box(
                    modifier = Modifier
                        .width(288.dp)
                        .height(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0D0A14))
                        .border(1.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
                    ) {
                        // Player avatar socket
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1D0D24))
                                .border(1.5.dp, Color(0xFFD4AF37), CircleShape)
                                .clickable {
                                    viewModel.activeColorPickerIndex =
                                        if (viewModel.activeColorPickerIndex == index) -1 else index
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            OccultPlayerAvatar(
                                playerColor = player.color,
                                playerIndex = index,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Player name field
                        TextField(
                            value = player.name,
                            onValueChange = { newName ->
                                viewModel.players[index] = player.copy(name = newName)
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        // Delete button
                        if (viewModel.players.size > 2) {
                            IconButton(
                                onClick = { viewModel.removePlayer(player) },
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFF7A7A8A),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Color picker
                AnimatedVisibility(
                    visible = viewModel.activeColorPickerIndex == index,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D0A14)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .width(288.dp)
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val otherPlayersColors = viewModel.players.filterIndexed { idx, _ -> idx != index }.map { it.color }
                            val availableGemstoneColors = GemstoneColors.filter { gemstone ->
                                gemstone.color != player.color && gemstone.color !in otherPlayersColors
                            }

                            availableGemstoneColors.forEach { gemstone ->
                                GemstoneButton(
                                    gemstoneColor = gemstone,
                                    isSelected = false,
                                    onClick = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.players[index] = player.copy(color = gemstone.color)
                                        viewModel.activeColorPickerIndex = -1
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// TAB CONTENT: SPELLBOOKS (with Magic Sigil Frames)
// ============================================================
@Composable
private fun SpellbooksTabContent(viewModel: GameViewModel, hapticFeedback: androidx.compose.ui.hapticfeedback.HapticFeedback) {
    val decksScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(decksScrollState)
    ) {
        Text(
            text = "Select Spell Books",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Serif,
            color = Color(0xFFD4AF37),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Spellbook sigil grid
        viewModel.availableSpellbooks.chunked(3).forEach { rowPacks ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowPacks.forEach { pack ->
                    val isSelected = viewModel.selectedSpellbooks.contains(pack)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { viewModel.togglePack(pack) }
                            .padding(4.dp)
                    ) {
                        MagicSigilFrame(isSelected = isSelected) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = pack,
                                tint = if (isSelected) Color(0xFFD4AF37) else Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = pack,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color.Gray,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            modifier = Modifier.width(70.dp)
                        )
                    }
                }
                // Fill remaining slots if row is not complete
                repeat(3 - rowPacks.size) {
                    Spacer(modifier = Modifier.width(80.dp))
                }
            }
        }

        FantasyOrnateDivider(modifier = Modifier.padding(vertical = 10.dp))

        // Kink Intensity Levels
        Text(
            text = "Kink Intensity Levels",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Serif,
            color = Color(0xFFD4AF37),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        SpiceLevel.values().forEach { level ->
            val isSelected = viewModel.selectedSpiceLevels.contains(level)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleSpiceLevel(level) }
                    .padding(vertical = 3.dp)
            ) {
                KinkyPadlockCheckbox(
                    checked = isSelected,
                    onCheckedChange = { viewModel.toggleSpiceLevel(level) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = level.displayName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color(0xFFD4AF37) else Color.Gray
                    )
                    Text(
                        text = level.description,
                        fontSize = 9.sp,
                        color = Color.LightGray,
                        lineHeight = 11.sp
                    )
                }
            }
        }
    }
}

// ============================================================
// TAB CONTENT: MAP
// ============================================================
@Composable
private fun MapTabContent(viewModel: GameViewModel, hapticFeedback: androidx.compose.ui.hapticfeedback.HapticFeedback) {
    Text(
        text = "Map Size:",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFD4AF37),
        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        listOf(
            Triple(4, "5x5", "Compact"),
            Triple(5, "6x6", "Standard"),
            Triple(6, "7x7", "Epic")
        ).forEach { (size, label, desc) ->
            val isSizeActive = viewModel.gridSize == size
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSizeActive) Color(0xFF1D0D24) else Color(0xFF0D0A14)
                ),
                border = BorderStroke(
                    1.5.dp,
                    if (isSizeActive) Color(0xFFD4AF37) else Color.White.copy(alpha = 0.2f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.gridSize = size
                        viewModel.regenerateBoard()
                    }
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSizeActive) Color.White else Color.Gray)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Infinite Mode Toggle
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text(
                "Infinite Play Session",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Play with no lap limits.",
                fontSize = 9.sp,
                color = Color.LightGray
            )
        }
        KinkyBoltToggle(
            checked = viewModel.isInfiniteMode,
            onCheckedChange = { viewModel.isInfiniteMode = it }
        )
    }

    if (!viewModel.isInfiniteMode) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Target Laps: ${viewModel.targetLaps}",
                fontSize = 13.sp,
                color = Color.White
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { if (viewModel.targetLaps > 1) viewModel.targetLaps-- }
                ) {
                    Icon(Icons.Default.Remove, "Fewer Laps", tint = Color(0xFFD4AF37))
                }
                Text(
                    text = viewModel.targetLaps.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(
                    onClick = { if (viewModel.targetLaps < 10) viewModel.targetLaps++ }
                ) {
                    Icon(Icons.Default.Add, "More Laps", tint = Color(0xFFD4AF37))
                }
            }
        }
    }

    FantasyOrnateDivider(modifier = Modifier.padding(vertical = 12.dp))

    // Animation Speed
    Text(
        text = "Meeple Hop Animation Speed:",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFD4AF37),
        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        listOf(
            Pair(1f, "1x Cinematic"),
            Pair(1.8f, "1.8x Snappy"),
            Pair(2.8f, "2.8x Fast")
        ).forEach { (speed, label) ->
            val isSelected = viewModel.gameSpeedMultiplier == speed
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFF1D0D24) else Color(0xFF0D0A14)
                ),
                border = BorderStroke(
                    1.5.dp,
                    if (isSelected) Color(0xFFD4AF37) else Color.White.copy(alpha = 0.2f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.gameSpeedMultiplier = speed
                    }
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ============================================================
// TAB CONTENT: SETTINGS
// ============================================================
@Composable
private fun SettingsTabContent(viewModel: GameViewModel, hapticFeedback: androidx.compose.ui.hapticfeedback.HapticFeedback) {
    val settingsScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(settingsScrollState)
    ) {
        // Board Randomization Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text(
                    "Randomize Board Layout",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Shuffle tiles at game start.",
                    fontSize = 9.sp,
                    color = Color.LightGray
                )
            }
            KinkyBoltToggle(
                checked = viewModel.isBoardRandomized,
                onCheckedChange = { viewModel.isBoardRandomized = it }
            )
        }

        FantasyOrnateDivider(modifier = Modifier.padding(vertical = 10.dp))

        // Quick Stats Display using Fantasy Progress Bars
        Text(
            text = "Session Stats",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Serif,
            color = Color(0xFFD4AF37),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        FantasyProgressBar(
            label = "Players",
            current = viewModel.players.size,
            maximum = 8,
            barColor = Color(0xFFE50914),
            modifier = Modifier.padding(vertical = 2.dp)
        )
        FantasyProgressBar(
            label = "Spellbooks",
            current = viewModel.selectedSpellbooks.size,
            maximum = viewModel.availableSpellbooks.size,
            barColor = Color(0xFF00D2FF),
            modifier = Modifier.padding(vertical = 2.dp)
        )
        FantasyProgressBar(
            label = "Spice Levels",
            current = viewModel.selectedSpiceLevels.size,
            maximum = SpiceLevel.values().size,
            barColor = Color(0xFFFFD700),
            modifier = Modifier.padding(vertical = 2.dp)
        )
        FantasyProgressBar(
            label = "Board Tiles",
            current = 4 * (viewModel.gridSize - 1),
            maximum = 28,
            barColor = Color(0xFF00D26A),
            modifier = Modifier.padding(vertical = 2.dp)
        )

        FantasyOrnateDivider(modifier = Modifier.padding(vertical = 10.dp))

        // Reset Game Button
        HexagonalFantasyButton(
            text = "RESET GAME",
            isPrimary = false,
            onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.resetGame()
            }
        )
    }
}
