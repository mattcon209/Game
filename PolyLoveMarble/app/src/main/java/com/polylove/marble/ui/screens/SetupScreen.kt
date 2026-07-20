package com.polylove.marble.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    
    SeductiveLeatherBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.setup_header),
                contentDescription = "Chains of Desire Header Banner",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Setup Player lobby card with flat color indicators (Style matched!)
                item {
                    KinkyCard(borderColor = SeductiveViolet.copy(alpha = 0.5f)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Session Partners (${viewModel.players.size}/8)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SilkPurple
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
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        viewModel.players.forEachIndexed { index, player ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(ObsidianBlack)
                                        .border(1.dp, SeductiveViolet.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    // Seductive Circular Occult Portrait - Glowing ring and canvas stylized portrait (replicated from sketch-up!)
                                    OccultPlayerAvatar(
                                        playerColor = player.color,
                                        playerName = player.name,
                                        playerIndex = index,
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clickable {
                                                viewModel.activeColorPickerIndex = 
                                                    if (viewModel.activeColorPickerIndex == index) -1 else index
                                            }
                                    )
                                    
                                    Spacer(modifier = Modifier.width(10.dp))
                                    
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
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                    
                                    Spacer(modifier = Modifier.width(6.dp))
                                    
                                    if (viewModel.players.size > 2) {
                                        IconButton(onClick = { viewModel.removePlayer(player) }) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = SteelGrey
                                            )
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
                                            .fillMaxWidth()
                                            .padding(start = 16.dp, bottom = 8.dp, end = 16.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Filter out own color AND any colors taken by other players to prevent duplicates!
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
                
                // Map Layout / Configurations
                item {
                    KinkyCard(borderColor = SeductiveViolet.copy(alpha = 0.4f)) {
                        Text(
                            text = "Map Layout & Settings",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SilkPurple,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Text(
                            text = "Map Size:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray
                        )

                        // Just 1 square with a drop-down menu instead of showing all options!
                        var sizeMenuExpanded by remember { mutableStateOf(false) }
                        val currentSizeInfo = listOf(
                            Triple(4, "Compact", "12 (4x4)"),
                            Triple(5, "Standard", "16 (5x5)"),
                            Triple(6, "Epic", "20 (6x6)"),
                            Triple(7, "Dungeon", "24 (7x7)"),
                            Triple(8, "Abyss", "28 (8x8)")
                        ).find { it.first == viewModel.gridSize } ?: Triple(5, "Standard", "16 (5x5)")

                        Box(modifier = Modifier.padding(vertical = 6.dp)) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = SeductiveViolet.copy(alpha = 0.4f)),
                                border = BorderStroke(1.5.dp, BrassGold),
                                modifier = Modifier
                                    .size(width = 120.dp, height = 50.dp)
                                    .clickable { 
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        sizeMenuExpanded = true 
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize().padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        currentSizeInfo.second,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        currentSizeInfo.third,
                                        fontSize = 10.sp,
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
                        
                        OrnateGothicDivider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        // Infinite Mode Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Infinite Play Session",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Play indefinitely with no lap limits. End session manually anytime.",
                                    fontSize = 10.sp,
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
                                        fontSize = 15.sp,
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
                    }
                }
                
                // Animation speed selection
                item {
                    KinkyCard(isShort = true) {
                        Text(
                            text = "Meeple Hop Animation Speed:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray,
                            modifier = Modifier.padding(bottom = 6.dp)
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
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            label,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else Color.Gray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Select Expansion Decks / Spell Books is COLLAPSIBLE!
                item {
                    var isSpellbooksExpanded by remember { mutableStateOf(true) }
                    KinkyCard(borderColor = BrassGold.copy(alpha = 0.5f)) {
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
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrassGold
                                )
                                Text(
                                    text = "Click locks to unlock/enable theme card decks!",
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
                }

                // Kink Intensity Levels Section (Levels 1 to 5!)
                item {
                    KinkyCard(borderColor = SeductiveViolet.copy(alpha = 0.5f)) {
                        Text(
                            text = "Kink Intensity Levels",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SilkPurple,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        SpiceLevel.values().forEach { level ->
                            val isSelected = viewModel.selectedSpiceLevels.contains(level)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.toggleSpiceLevel(level) }
                                    .padding(vertical = 6.dp)
                            ) {
                                KinkyPadlockCheckbox(
                                    checked = isSelected,
                                    onCheckedChange = { viewModel.toggleSpiceLevel(level) }
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = level.displayName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) LatexCrimson else Color.Gray
                                    )
                                    Text(
                                        text = level.description,
                                        fontSize = 10.sp,
                                        color = Color.LightGray,
                                        lineHeight = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Creation Mode Editor Button (Updated to use your beautiful custom image!)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .clip(RoundedCornerShape(29.dp)) // Pill-shape clipping to hide outer background!
                            .border(2.dp, BrassGold, RoundedCornerShape(29.dp))
                            .shadow(12.dp, RoundedCornerShape(29.dp), ambientColor = BrassGold)
                            .clickable {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.currentScreen = GameScreen.Editor
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.open_creation_btn),
                            contentDescription = "Open Creation Mode",
                            contentScale = ContentScale.Crop, // Crops and fits perfectly
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LeatherStrapButton(
                text = "Begin Session",
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
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
