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
    
    // Tab State: LOBBY, MAP, DECKS
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
            
            // 1. ORIGINAL ILLUSTRATED ATMOSPHERIC HEADER BANNER (No clipping, completely responsive!)
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
            
            // 2. PREMIUM TABBED NAVIGATION CONTAINER: Obsidian leather card panel with gold-gilded corners!
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(440.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                LeatherDarkPurple.copy(alpha = 0.92f),
                                ObsidianBlack.copy(alpha = 0.96f)
                            )
                        )
                    )
                    .border(2.dp, BrassGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .shadow(12.dp, RoundedCornerShape(16.dp), ambientColor = BrassGold),
                contentAlignment = Alignment.TopCenter
            ) {
                // Draw elegant gold gilded corner filigrees!
                Canvas(modifier = Modifier.matchParentSize()) {
                    val w = size.width
                    val h = size.height
                    
                    // Top-Left Corner
                    drawGildedCorner(x = 0f, y = 0f, isLeft = true, isTop = true)
                    // Top-Right Corner
                    drawGildedCorner(x = w, y = 0f, isLeft = false, isTop = true)
                    // Bottom-Left Corner
                    drawGildedCorner(x = 0f, y = h, isLeft = true, isTop = false)
                    // Bottom-Right Corner
                    drawGildedCorner(x = w, y = h, isLeft = false, isTop = false)
                }
                
                Column(modifier = Modifier.fillMaxSize()) {
                    // TAB NAVIGATION ROW AT TOP OF THE CARD
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(Color.Black.copy(alpha = 0.4f))
                    ) {
                        listOf("LOBBY", "MAP", "DECKS").forEach { tab ->
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
                                        if (isActive) {
                                            Brush.verticalGradient(
                                                colors = listOf(LatexCrimson.copy(alpha = 0.25f), Color.Transparent)
                                            )
                                        } else {
                                            Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Transparent))
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tab,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.5.sp,
                                    color = if (isActive) Color.White else Color.Gray,
                                    modifier = Modifier.padding(bottom = if (isActive) 0.dp else 2.dp)
                                )
                                // Active Bottom underline
                                if (isActive) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth()
                                            .height(3.dp)
                                            .background(LatexCrimson)
                                            .shadow(4.dp, ambientColor = LatexCrimson)
                                    )
                                }
                            }
                        }
                    }
                    
                    // CONTENT PANEL BASED ON ACTIVE TAB (Padded inside the parchment!)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (activeTab) {
                            "LOBBY" -> {
                                // === TAB 1: SESSION PARTNERS (LOBBY) ===
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Session Partners (${viewModel.players.size}/8)",
                                        fontSize = 15.sp,
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
                                
                                // Scrollable partners list inside the card - never stretches!
                                val playerScrollState = rememberScrollState()
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(290.dp)
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
                            "MAP" -> {
                                // === TAB 2: MAP LAYOUT & SETTINGS ===
                                Text(
                                    text = "Map Size:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrassGold,
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                                )
                                
                                val currentSizeInfo = listOf(
                                    Triple(4, "Compact", "12 (4x4)"),
                                    Triple(5, "Standard", "16 (5x5)"),
                                    Triple(6, "Epic", "20 (6x6)"),
                                    Triple(7, "Dungeon", "24 (7x7)"),
                                    Triple(8, "Abyss", "28 (8x8)")
                                ).find { it.first == viewModel.gridSize } ?: Triple(5, "Standard", "16 (5x5)")
                                
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
                                                containerColor = if (isSizeActive) SeductiveViolet.copy(alpha = 0.4f) else ObsidianBlack
                                            ),
                                            border = BorderStroke(
                                                1.5.dp,
                                                if (isSizeActive) BrassGold else SeductiveViolet.copy(alpha = 0.2f)
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
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrassGold,
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
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
                            "DECKS" -> {
                                // === TAB 3: SELECT SPELL BOOKS & KINK LEVELS ===
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
                                        color = BrassGold,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
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
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else Color.Gray
                                            )
                                        }
                                    }
                                    
                                    OrnateGothicDivider(modifier = Modifier.padding(vertical = 12.dp))
                                    
                                    Text(
                                        text = "Kink Intensity Levels",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = FontFamily.Serif,
                                        color = BrassGold,
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
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // 3. PREMIUM TACTILE 3D INTERACTIVE STRAP NAVIGATION BUTTONS
            
            // === SPELLBOOK EDITOR BUTTON ===
            PremiumGothicButton(
                text = "Spellbook Editor",
                isPurple = false,
                onClick = {
                    viewModel.currentScreen = GameScreen.Editor
                }
            )
            
            Spacer(modifier = Modifier.height(10.dp))
            
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
