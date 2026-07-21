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
import com.polylove.marble.ui.ChainsOfDesireLogo
import com.polylove.marble.ui.GameScreen
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.components.*
import com.polylove.marble.ui.theme.*

@Composable
fun SetupScreen(viewModel: GameViewModel) {
    val hapticFeedback = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    
    SeductiveLeatherBackground {
        // Main scrollable column to fit all redesigned components responsively on any screen!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            
            // 1. PREMIUM BRANDED LOGO HEADER (No clipping, completely vector-rendered!)
            ChainsOfDesireLogo(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )
            
            OrnateGothicDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            // 2. MODULAR REDESIGNED SECTIONS (Using GothicPremiumCard panels with gold-gilded corners!)
            
            // === CARD 1: SESSION PARTNERS (LOBBY) ===
            GothicPremiumCard(borderColor = SeductiveViolet.copy(alpha = 0.5f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Session Partners (${viewModel.players.size}/8)",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = BrassGold
                    )
                    IconButton(onClick = { viewModel.addPlayer() }, enabled = viewModel.players.size < 8) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = "Add Player",
                            tint = if (viewModel.players.size < 8) LatexCrimson else Color.Gray,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                // Scrollable list of partners - never stretches the card!
                val playerScrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp)
                        .verticalScroll(playerScrollState),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    viewModel.players.forEachIndexed { index, player ->
                        Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                            // Player bar backdrop - exactly 288.dp x 58.dp to match un-stretched ratio (1310x264)!
                            Box(
                                modifier = Modifier
                                    .width(288.dp)
                                    .height(58.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.session_partner_bar),
                                    contentDescription = "Player Row Backdrop",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    // Left aligned circular portrait socket (occupies exactly 58dp x 58dp)
                                    Box(
                                        modifier = Modifier
                                            .width(58.dp)
                                            .fillMaxHeight(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        OccultPlayerAvatar(
                                            playerColor = player.color,
                                            playerIndex = index,
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clickable {
                                                    viewModel.activeColorPickerIndex = 
                                                        if (viewModel.activeColorPickerIndex == index) -1 else index
                                                }
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.width(4.dp))
                                    
                                    // Player name text field
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
                                            modifier = Modifier.padding(end = 16.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = SteelGrey,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Unique Player Colors selection picker
                            AnimatedVisibility(
                                visible = viewModel.activeColorPickerIndex == index,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = ObsidianBlack),
                                    border = BorderStroke(1.dp, SeductiveViolet.copy(alpha = 0.3f)),
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
            
            Spacer(modifier = Modifier.height(14.dp))
            
            // === CARD 2: MAP LAYOUT & SETTINGS ===
            GothicPremiumCard(borderColor = SeductiveViolet.copy(alpha = 0.4f)) {
                Text(
                    text = "Map Settings & Layout",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    color = BrassGold,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Map Size:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )

                    var sizeMenuExpanded by remember { mutableStateOf(false) }
                    val currentSizeInfo = listOf(
                        Triple(4, "Compact", "12 (4x4)"),
                        Triple(5, "Standard", "16 (5x5)"),
                        Triple(6, "Epic", "20 (6x6)"),
                        Triple(7, "Dungeon", "24 (7x7)"),
                        Triple(8, "Abyss", "28 (8x8)")
                    ).find { it.first == viewModel.gridSize } ?: Triple(5, "Standard", "16 (5x5)")

                    Box {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SeductiveViolet.copy(alpha = 0.4f)),
                            border = BorderStroke(1.5.dp, BrassGold),
                            modifier = Modifier
                                .size(width = 110.dp, height = 44.dp)
                                .clickable { 
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    sizeMenuExpanded = true 
                                }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                        Text(
                                            currentSizeInfo.second,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            currentSizeInfo.third,
                                            fontSize = 9.sp,
                                            color = Color.LightGray,
                                            textAlign = TextAlign.Center
                                        )
                            }
                        }

                        DropdownMenu(
                            expanded = sizeMenuExpanded,
                            onDismissRequest = { sizeMenuExpanded = false },
                            modifier = Modifier.background(LeatherDarkPurple)
                        ) {
                            listOf(
                                Triple(4, "Compact", "12 (4x4)"),
                                Triple(5, "Standard", "16 (5x5)"),
                                Triple(6, "Epic", "20 (6x6)"),
                                Triple(7, "Dungeon", "24 (7x7)"),
                                Triple(8, "Abyss", "28 (8x8)")
                            ).forEach { (size, label, desc) ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(label, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                                            Text(desc, color = Color.Gray, fontSize = 10.sp)
                                        }
                                    },
                                    onClick = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.gridSize = size
                                        viewModel.regenerateBoard()
                                        sizeMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
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
                            text = "Play indefinitely with no lap limits.",
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
                    Spacer(modifier = Modifier.height(8.dp))
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
                                Icon(Icons.Default.Remove, "Fewer Laps", tint = LatexCrimson)
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
                                Icon(Icons.Default.Add, "More Laps", tint = LatexCrimson)
                            }
                        }
                    }
                }
                
                OrnateGothicDivider(modifier = Modifier.padding(vertical = 12.dp))
                
                // === Animation Speed selection ===
                Text(
                    text = "Meeple Hop Animation Speed:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    color = BrassGold,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
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
                                containerColor = if (isSelected) SeductiveViolet.copy(alpha = 0.4f) else ObsidianBlack
                            ),
                            border = BorderStroke(
                                1.5.dp,
                                if (isSelected) BrassGold else SeductiveViolet.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.gameSpeedMultiplier = speed
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
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
            
            Spacer(modifier = Modifier.height(14.dp))
            
            // === CARD 3: SELECT SPELL BOOKS (Collapsible) ===
            var isSpellbooksExpanded by remember { mutableStateOf(true) }
            GothicPremiumCard(borderColor = BrassGold.copy(alpha = 0.5f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            isSpellbooksExpanded = !isSpellbooksExpanded
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Select Spell Books",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Serif,
                            color = BrassGold
                        )
                        Text(
                            text = "Unlock/enable custom theme card decks!",
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                    }
                    Icon(
                        if (isSpellbooksExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle Section",
                        tint = BrassGold
                    )
                }
                
                AnimatedVisibility(
                    visible = isSpellbooksExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                        viewModel.availableSpellbooks.forEach { pack ->
                            val isSelected = viewModel.selectedSpellbooks.contains(pack)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.togglePack(pack) }
                                    .padding(vertical = 4.dp)
                            ) {
                                KinkyPadlockCheckbox(
                                    checked = isSelected,
                                    onCheckedChange = { viewModel.togglePack(pack) }
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = pack,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color.Gray
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(14.dp))
            
            // === CARD 4: KINK INTENSITY LEVELS ===
            GothicPremiumCard(borderColor = SeductiveViolet.copy(alpha = 0.5f)) {
                Text(
                    text = "Kink Intensity Levels",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    color = BrassGold,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )
                
                val levelScrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .verticalScroll(levelScrollState),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SpiceLevel.values().forEach { level ->
                        val isSelected = viewModel.selectedSpiceLevels.contains(level)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.toggleSpiceLevel(level) }
                                .padding(vertical = 4.dp)
                        ) {
                            KinkyPadlockCheckbox(
                                checked = isSelected,
                                        onCheckedChange = { viewModel.toggleSpiceLevel(level) }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = level.displayName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) LatexCrimson else Color.Gray
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
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 3. PREMIUM RESPONSIVE 3D CLICKABLE BUTTONS
            
            // === SPELLBOOK EDITOR BUTTON ===
            PremiumGothicButton(
                text = "Spellbook Editor",
                isPurple = false,
                onClick = {
                    viewModel.currentScreen = GameScreen.Editor
                }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // === BEGIN SESSION BUTTON ===
            PremiumGothicButton(
                text = "Begin Session",
                isPurple = true,
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
