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
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.polylove.marble.R
import com.polylove.marble.ui.GameScreen
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.components.*
import com.polylove.marble.ui.theme.*

@Composable
fun SetupScreen(viewModel: GameViewModel) {
    val hapticFeedback = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    var activeTab by remember { mutableStateOf("LOBBY") }

    SeductiveLeatherBackground {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(12.dp))

            Image(painter = painterResource(id = R.drawable.setup_header), contentDescription = "Header", contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(8.dp)).border(2.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(8.dp)))

            Spacer(modifier = Modifier.height(12.dp))

            // Tab Navigation
            Row(modifier = Modifier.fillMaxWidth().height(44.dp).clip(RoundedCornerShape(6.dp)).background(DarkSurface).border(1.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(6.dp)),
                horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                listOf("LOBBY", "SPELLBOOKS", "MAP", "SETTINGS").forEach { tab ->
                    val isActive = activeTab == tab
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); activeTab = tab }
                        .background(if (isActive) CrimsonDeep else Color.Transparent), contentAlignment = Alignment.Center) {
                        Text(text = tab, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.sp, color = if (isActive) Color.White else TextMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            GlassPanel {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = when (activeTab) { "LOBBY" -> "SESSION PARTNERS"; "SPELLBOOKS" -> "SPELL BOOKS"; "MAP" -> "MAP CONFIGURATION"; "SETTINGS" -> "GAME SETTINGS"; else -> "" },
                        fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GoldPrimary, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), textAlign = TextAlign.Center)
                    OrnateGothicDivider()
                    Spacer(modifier = Modifier.height(12.dp))
                    when (activeTab) { "LOBBY" -> LobbyContent(viewModel, hapticFeedback); "SPELLBOOKS" -> SpellbookContent(viewModel, hapticFeedback); "MAP" -> MapContent(viewModel, hapticFeedback); "SETTINGS" -> SettingsContent(viewModel, hapticFeedback) }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PremiumGothicButton(text = "SPELLBOOK EDITOR", isPurple = false, onClick = { viewModel.currentScreen = GameScreen.Editor })
            Spacer(modifier = Modifier.height(8.dp))
            PremiumGothicButton(text = "BEGIN SESSION", isPurple = true, onClick = {
                viewModel.board.clear()
                viewModel.board.addAll(BoardCreator.createBoardForPacks(viewModel.gridSize, viewModel.selectedSpellbooks.toSet(), viewModel.isBoardRandomized))
                viewModel.currentPlayerIndex = 0
                viewModel.currentScreen = GameScreen.Board
            })
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LobbyContent(viewModel: GameViewModel, hapticFeedback: androidx.compose.ui.hapticfeedback.HapticFeedback) {
    val playerScrollState = rememberScrollState()
    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Text(text = "Session Partners (${viewModel.players.size}/8)", fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, color = GoldPrimary)
        IconButton(onClick = { viewModel.addPlayer() }, enabled = viewModel.players.size < 8) {
            Icon(Icons.Default.AddCircle, "Add Player", tint = if (viewModel.players.size < 8) GoldPrimary else TextMuted, modifier = Modifier.size(24.dp))
        }
    }
    Column(modifier = Modifier.fillMaxWidth().height(240.dp).verticalScroll(playerScrollState), verticalArrangement = Arrangement.spacedBy(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        viewModel.players.forEachIndexed { index, player ->
            Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.width(260.dp).height(52.dp).clip(RoundedCornerShape(8.dp)).background(DarkSurface).border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(8.dp))) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
                        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(DarkCardBg).border(1.5.dp, GoldPrimary, CircleShape)
                            .clickable { viewModel.activeColorPickerIndex = if (viewModel.activeColorPickerIndex == index) -1 else index }, contentAlignment = Alignment.Center) {
                            OccultPlayerAvatar(playerColor = player.color, playerIndex = index, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(value = player.name, onValueChange = { viewModel.players[index] = player.copy(name = it) },
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                            textStyle = LocalTextStyle.current.copy(fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif), modifier = Modifier.weight(1f))
                        if (viewModel.players.size > 2) {
                            IconButton(onClick = { viewModel.removePlayer(player) }) { Icon(Icons.Default.Delete, "Delete", tint = TextMuted, modifier = Modifier.size(18.dp)) }
                        }
                    }
                }
                AnimatedVisibility(visible = viewModel.activeColorPickerIndex == index) {
                    Card(colors = CardDefaults.cardColors(containerColor = DarkSurface), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)), shape = RoundedCornerShape(8.dp), modifier = Modifier.width(260.dp).padding(vertical = 4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(6.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            val otherColors = viewModel.players.filterIndexed { idx, _ -> idx != index }.map { it.color }
                            GemstoneColors.filter { it.color != player.color && it.color !in otherColors }.forEach { gemstone ->
                                GemstoneButton(gemstoneColor = gemstone, isSelected = false, onClick = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); viewModel.players[index] = player.copy(color = gemstone.color); viewModel.activeColorPickerIndex = -1 })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpellbookContent(viewModel: GameViewModel, hapticFeedback: androidx.compose.ui.hapticfeedback.HapticFeedback) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Select Spell Books", fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, color = GoldPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        viewModel.availableSpellbooks.chunked(2).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { pack ->
                    val isSelected = viewModel.selectedSpellbooks.contains(pack)
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { viewModel.togglePack(pack) }.padding(4.dp)) {
                        Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)).background(if (isSelected) CrimsonDeep else DarkSurface).border(2.dp, if (isSelected) CrimsonGlow else Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Lock, pack, tint = if (isSelected) CrimsonGlow else Color.White, modifier = Modifier.size(20.dp))
                        }
                        Text(pack, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else TextMuted, textAlign = TextAlign.Center, modifier = Modifier.width(70.dp).padding(top = 4.dp))
                    }
                }
                if (row.size == 1) Spacer(modifier = Modifier.width(80.dp))
            }
        }
        OrnateGothicDivider()
        Spacer(modifier = Modifier.height(8.dp))
        Text("Kink Intensity Levels", fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, color = GoldPrimary)
        SpiceLevel.values().forEach { level ->
            val isSelected = viewModel.selectedSpiceLevels.contains(level)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { viewModel.toggleSpiceLevel(level) }.padding(vertical = 3.dp)) {
                KinkyPadlockCheckbox(checked = isSelected, onCheckedChange = { viewModel.toggleSpiceLevel(level) })
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(level.displayName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) CrimsonGlow else TextMuted)
                    Text(level.description, fontSize = 8.sp, color = TextMuted, lineHeight = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun MapContent(viewModel: GameViewModel, hapticFeedback: androidx.compose.ui.hapticfeedback.HapticFeedback) {
    Text("Map Size:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldPrimary, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf(Triple(4, "5x5", ""), Triple(5, "6x6", ""), Triple(6, "7x7", "")).forEach { (size, label, _) ->
            val isActive = viewModel.gridSize == size
            Card(colors = CardDefaults.cardColors(containerColor = if (isActive) CrimsonDeep else DarkSurface),
                border = BorderStroke(1.5.dp, if (isActive) GoldPrimary else Color.White.copy(alpha = 0.2f)),
                modifier = Modifier.weight(1f).clickable { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); viewModel.gridSize = size; viewModel.regenerateBoard() }) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                    Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isActive) Color.White else TextMuted)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text("Infinite Play Session", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Play with no lap limits.", fontSize = 9.sp, color = TextMuted)
        }
        KinkyBoltToggle(checked = viewModel.isInfiniteMode, onCheckedChange = { viewModel.isInfiniteMode = it })
    }
}

@Composable
private fun SettingsContent(viewModel: GameViewModel, hapticFeedback: androidx.compose.ui.hapticfeedback.HapticFeedback) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text("Randomize Board Layout", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Shuffle tiles at game start.", fontSize = 9.sp, color = TextMuted)
        }
        KinkyBoltToggle(checked = viewModel.isBoardRandomized, onCheckedChange = { viewModel.isBoardRandomized = it })
    }
    OrnateGothicDivider()
    Text("Session Stats", fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, color = GoldPrimary, modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(8.dp))
    Text("Players: ${viewModel.players.size} / 8", fontSize = 11.sp, color = Color.White, modifier = Modifier.padding(vertical = 2.dp))
    Text("Spellbooks: ${viewModel.selectedSpellbooks.size} / ${viewModel.availableSpellbooks.size}", fontSize = 11.sp, color = Color.White, modifier = Modifier.padding(vertical = 2.dp))
    Text("Spice Levels: ${viewModel.selectedSpiceLevels.size} / ${SpiceLevel.values().size}", fontSize = 11.sp, color = Color.White, modifier = Modifier.padding(vertical = 2.dp))
    Text("Board Tiles: ${4 * (viewModel.gridSize - 1)}", fontSize = 11.sp, color = Color.White, modifier = Modifier.padding(vertical = 2.dp))
    OrnateGothicDivider()
    PremiumGothicButton(text = "RESET GAME", isPurple = false, onClick = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); viewModel.resetGame() })
}
