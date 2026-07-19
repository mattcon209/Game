package com.polylove.marble.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.polylove.marble.game.*
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.components.TileVectorIcon
import com.polylove.marble.ui.theme.*

@Composable
fun TileEditorTab(
    viewModel: GameViewModel,
    hapticFeedback: HapticFeedback,
    modifier: Modifier = Modifier
) {
    // Local Tile Customizer States
    var selectedTileForEdit by remember { mutableStateOf<Tile?>(null) }
    var showCustomizerDialog by remember { mutableStateOf(false) }
    
    var tileActionSelect by remember { mutableStateOf(TileAction.NORMAL_CARD) }
    var tileCardCategorySelect by remember { mutableStateOf(TileType.TRUTH) }
    var tileMoveSpacesValueSelect by remember { mutableStateOf("2") }
    var tileCustomMessageSelect by remember { mutableStateOf("") }
    var tileLabelSelect by remember { mutableStateOf("Truth Space") }
    var tileEmojiSelect by remember { mutableStateOf("⛓️") }

    Column(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            // 1. ACTIVE PILLARS SECTION (GOLD-ROSE HIGHLIGHT)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    "ACTIVE SUMMONING PILLARS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = BrassGold,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }
            
            val activeList = viewModel.board
            gridItems(activeList) { tile ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = LeatherDarkPurple),
                    border = BorderStroke(1.5.dp, BrassGold), // Gold-rose highlight
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            selectedTileForEdit = tile
                            tileLabelSelect = tile.label
                            tileActionSelect = tile.action
                            tileCardCategorySelect = tile.cardCategory
                            tileMoveSpacesValueSelect = tile.moveSpacesValue.toString()
                            tileCustomMessageSelect = tile.customMessageText
                            tileEmojiSelect = tile.emoji
                            showCustomizerDialog = true
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "PILLAR #${tile.index}",
                            fontSize = 8.sp,
                            color = BrassGold,
                            fontWeight = FontWeight.Bold
                        )
                        
                        TileVectorIcon(type = tile.cardCategory, action = tile.action, modifier = Modifier.size(24.dp))
                        
                        Text(
                            text = tile.label.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Text(
                            text = "ACTIVE",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00FFCC)
                        )
                    }
                }
            }
            
            // 2. INACTIVE PILLARS SECTION (GREYED OUT AT BOTTOM)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    "INACTIVE PILLARS (POOL)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = SteelGrey,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
            }
            
            val inactiveList = viewModel.inactivePillars
            gridItems(inactiveList) { tile ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = LeatherDarkPurple.copy(alpha = 0.5f)),
                    border = BorderStroke(1.5.dp, SteelGrey.copy(alpha = 0.4f)), // Greyed out steel border
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            selectedTileForEdit = tile
                            tileLabelSelect = tile.label
                            tileActionSelect = tile.action
                            tileCardCategorySelect = tile.cardCategory
                            tileMoveSpacesValueSelect = tile.moveSpacesValue.toString()
                            tileCustomMessageSelect = tile.customMessageText
                            tileEmojiSelect = tile.emoji
                            showCustomizerDialog = true
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "UNUSED",
                            fontSize = 8.sp,
                            color = SteelGrey,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Box(modifier = Modifier.alpha(0.5f)) {
                            TileVectorIcon(type = tile.cardCategory, action = tile.action, modifier = Modifier.size(24.dp))
                        }
                        
                        Text(
                            text = tile.label.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Text(
                            text = "INACTIVE",
                            fontSize = 8.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

    // Customizer Dialog Overlay
    if (showCustomizerDialog && selectedTileForEdit != null) {
        val editingTile = selectedTileForEdit!!
        val isCurrentlyActive = viewModel.board.any { it.index == editingTile.index }
        
        Dialog(onDismissRequest = { showCustomizerDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = LeatherDarkPurple),
                border = BorderStroke(1.5.dp, BrassGold),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CUSTOMIZE PILLAR RUNE",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = BrassGold
                    )
                    Text(
                        text = if (isCurrentlyActive) "Active Board Pillar #${editingTile.index}" else "Inactive Pool Card",
                        fontSize = 10.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    HorizontalDivider(color = SeductiveViolet.copy(alpha = 0.25f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text("Pillar Label / Inscription:", fontSize = 11.sp, color = BrassGold)
                            TextField(
                                value = tileLabelSelect,
                                onValueChange = { tileLabelSelect = it },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = ObsidianBlack,
                                    unfocusedContainerColor = ObsidianBlack,
                                    focusedTextColor = Color.White
                                ),
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            )
                        }
                        
                        item {
                            Text("Assign Arcane Spell Effect:", fontSize = 11.sp, color = BrassGold)
                            var actionExpanded by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Button(
                                    onClick = { actionExpanded = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = ObsidianBlack),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(tileActionSelect.displayName, color = Color.White, fontSize = 12.sp)
                                }
                                DropdownMenu(expanded = actionExpanded, onDismissRequest = { actionExpanded = false }) {
                                    TileAction.values().forEach { action ->
                                        DropdownMenuItem(
                                            text = { Text(action.displayName) },
                                            onClick = {
                                                tileActionSelect = action
                                                actionExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        
                        item {
                            Text("Arcane Spell Theme:", fontSize = 11.sp, color = BrassGold)
                            var categoryExpanded by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Button(
                                    onClick = { categoryExpanded = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = ObsidianBlack),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(tileCardCategorySelect.displayName, color = Color.White, fontSize = 12.sp)
                                }
                                DropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                                    listOf(TileType.TRUTH, TileType.DARE, TileType.KINKY_LIGHT, TileType.TEASE_DENIAL, TileType.IMPACT_DOM, TileType.GROUP_BDSM).forEach { type ->
                                        DropdownMenuItem(
                                            text = { Text(type.displayName) },
                                            onClick = {
                                                tileCardCategorySelect = type
                                                categoryExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        
                        if (tileActionSelect == TileAction.MOVE_SPACES) {
                            item {
                                Text("Spaces to Move (forward/backward):", fontSize = 11.sp, color = BrassGold)
                                TextField(
                                    value = tileMoveSpacesValueSelect,
                                    onValueChange = { tileMoveSpacesValueSelect = it },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = ObsidianBlack,
                                        unfocusedContainerColor = ObsidianBlack,
                                        focusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                )
                            }
                        }
                        
                        if (tileActionSelect == TileAction.CUSTOM_MESSAGE) {
                            item {
                                Text("Custom Rule Command Text:", fontSize = 11.sp, color = BrassGold)
                                TextField(
                                    value = tileCustomMessageSelect,
                                    onValueChange = { tileCustomMessageSelect = it },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = ObsidianBlack,
                                        unfocusedContainerColor = ObsidianBlack,
                                        focusedTextColor = Color.White
                                    ),
                                    placeholder = { Text("Use placeholders like {player}, {target1}...") },
                                    modifier = Modifier.fillMaxWidth().height(64.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // ACTION BUTTONS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Clone Button (DURABLE CLONING!)
                        Button(
                            onClick = {
                                val cloned = Tile(
                                    index = 100 + viewModel.inactivePillars.size,
                                    label = "${tileLabelSelect} (Clone)",
                                    action = tileActionSelect,
                                    cardCategory = tileCardCategorySelect,
                                    moveSpacesValue = tileMoveSpacesValueSelect.toIntOrNull() ?: 2,
                                    customMessageText = tileCustomMessageSelect,
                                    emoji = tileEmojiSelect
                                )
                                viewModel.inactivePillars.add(cloned)
                                showCustomizerDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LeatherDarkPurple),
                            border = BorderStroke(1.dp, BrassGold),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Clone Pillar", color = BrassGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        
                        // Activate / Deactivate Toggle
                        if (isCurrentlyActive) {
                            Button(
                                onClick = {
                                    if (viewModel.board.size > 4) {
                                        viewModel.board.remove(editingTile)
                                        // Re-index remaining board tiles to preserve the loop!
                                        val reindexed = ArrayList<Tile>()
                                        viewModel.board.forEachIndexed { idx, tile ->
                                            reindexed.add(tile.copy(index = idx))
                                        }
                                        viewModel.board.clear()
                                        viewModel.board.addAll(reindexed)
                                        
                                        viewModel.inactivePillars.add(editingTile)
                                        showCustomizerDialog = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42000A)),
                                border = BorderStroke(1.dp, LatexCrimson),
                                modifier = Modifier.weight(1f),
                                enabled = viewModel.board.size > 4
                            ) {
                                Text("Deactivate", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = {
                                    val newIndex = viewModel.board.size
                                    val activated = editingTile.copy(index = newIndex)
                                    viewModel.board.add(activated)
                                    viewModel.inactivePillars.remove(editingTile)
                                    showCustomizerDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SeductiveViolet),
                                border = BorderStroke(1.dp, SilkPurple),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Activate", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    
                    // THIRD ROW: Delete custom tile if index >= 100
                    if (editingTile.index >= 100) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (isCurrentlyActive) {
                                    viewModel.board.remove(editingTile)
                                    // Re-index remaining board tiles to preserve the loop!
                                    val reindexed = ArrayList<Tile>()
                                    viewModel.board.forEachIndexed { idx, tile ->
                                        reindexed.add(tile.copy(index = idx))
                                    }
                                    viewModel.board.clear()
                                    viewModel.board.addAll(reindexed)
                                } else {
                                    viewModel.inactivePillars.remove(editingTile)
                                }
                                showCustomizerDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
                            border = BorderStroke(1.5.dp, LatexCrimson),
                            modifier = Modifier.fillMaxWidth().height(36.dp)
                        ) {
                            Text("DELETE CUSTOM PILLAR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showCustomizerDialog = false },
                            border = BorderStroke(1.dp, SteelGrey),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Cancel", color = Color.LightGray)
                        }
                        
                        Button(
                            onClick = {
                                // Save changes back
                                if (isCurrentlyActive) {
                                    val idx = viewModel.board.indexOfFirst { it.index == editingTile.index }
                                    if (idx != -1) {
                                        viewModel.board[idx] = editingTile.copy(
                                            label = tileLabelSelect,
                                            action = tileActionSelect,
                                            cardCategory = tileCardCategorySelect,
                                            moveSpacesValue = tileMoveSpacesValueSelect.toIntOrNull() ?: 2,
                                            customMessageText = tileCustomMessageSelect,
                                            emoji = tileEmojiSelect
                                        )
                                    }
                                } else {
                                    val idx = viewModel.inactivePillars.indexOfFirst { it.index == editingTile.index }
                                    if (idx != -1) {
                                        viewModel.inactivePillars[idx] = editingTile.copy(
                                            label = tileLabelSelect,
                                            action = tileActionSelect,
                                            cardCategory = tileCardCategorySelect,
                                            moveSpacesValue = tileMoveSpacesValueSelect.toIntOrNull() ?: 2,
                                            customMessageText = tileCustomMessageSelect,
                                            emoji = tileEmojiSelect
                                        )
                                    }
                                }
                                showCustomizerDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LatexCrimson),
                            modifier = Modifier.weight(2f)
                        ) {
                            Text("Save Pillar", color = Color.White, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}
