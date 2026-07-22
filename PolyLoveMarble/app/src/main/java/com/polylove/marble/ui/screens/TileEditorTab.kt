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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.polylove.marble.game.*
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.components.*
import com.polylove.marble.ui.theme.*

@Composable
fun TileEditorTab(viewModel: GameViewModel, hapticFeedback: HapticFeedback, modifier: Modifier = Modifier) {
    var selectedTileForEdit by remember { mutableStateOf<Tile?>(null) }
    var showCustomizerDialog by remember { mutableStateOf(false) }
    var tileActionSelect by remember { mutableStateOf(TileAction.NORMAL_CARD) }
    var tileCardCategorySelect by remember { mutableStateOf(TileType.TRUTH) }
    var tileMoveSpacesValueSelect by remember { mutableStateOf("2") }
    var tileCustomMessageSelect by remember { mutableStateOf("") }
    var tileLabelSelect by remember { mutableStateOf("Truth Space") }

    Column(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text("ACTIVE SUMMONING PILLARS", fontSize = 11.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Serif, color = GoldPrimary, modifier = Modifier.padding(top = 4.dp, bottom = 4.dp))
            }
            
            gridItems(viewModel.board) { tile ->
                GlassPanel(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(6.dp).clickable {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            selectedTileForEdit = tile; tileLabelSelect = tile.label; tileActionSelect = tile.action
                            tileCardCategorySelect = tile.cardCategory; tileMoveSpacesValueSelect = tile.moveSpacesValue.toString()
                            tileCustomMessageSelect = tile.customMessageText; showCustomizerDialog = true
                        },
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("PILLAR #${tile.index}", fontSize = 8.sp, color = GoldPrimary, fontWeight = FontWeight.Bold)
                        MagicCircleSigil(variant = getSigilVariant(tile.cardCategory, tile.action), color = Color(android.graphics.Color.parseColor(tile.cardCategory.colorHex)), size = 36)
                        Text(tile.label.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("ACTIVE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00FFCC))
                    }
                }
            }
            
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text("INACTIVE PILLARS (POOL)", fontSize = 11.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Serif, color = TextMuted, modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
            }
            
            gridItems(viewModel.inactivePillars) { tile ->
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(8.dp)).background(DarkSurface.copy(alpha = 0.5f)).border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(6.dp).clickable {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            selectedTileForEdit = tile; tileLabelSelect = tile.label; tileActionSelect = tile.action
                            tileCardCategorySelect = tile.cardCategory; tileMoveSpacesValueSelect = tile.moveSpacesValue.toString()
                            tileCustomMessageSelect = tile.customMessageText; showCustomizerDialog = true
                        },
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("UNUSED", fontSize = 8.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                        Box(modifier = Modifier.alpha(0.5f)) { MagicCircleSigil(variant = getSigilVariant(tile.cardCategory, tile.action), color = Color.Gray, size = 36) }
                        Text(tile.label.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("INACTIVE", fontSize = 8.sp, color = Color.Gray)
                    }
                }
            }
        }
    }

    if (showCustomizerDialog && selectedTileForEdit != null) {
        val editingTile = selectedTileForEdit!!
        val isCurrentlyActive = viewModel.board.any { it.index == editingTile.index }
        Dialog(onDismissRequest = { showCustomizerDialog = false }) {
            Card(colors = CardDefaults.cardColors(containerColor = DarkSurface), border = BorderStroke(1.5.dp, GoldPrimary), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("CUSTOMIZE PILLAR RUNE", fontSize = 16.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Serif, color = GoldPrimary)
                    Text(if (isCurrentlyActive) "Active Board Pillar #${editingTile.index}" else "Inactive Pool Card", fontSize = 10.sp, color = TextMuted, modifier = Modifier.padding(bottom = 12.dp))
                    OrnateGothicDivider()
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f, fill = false), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        item { Text("Pillar Label / Inscription:", fontSize = 11.sp, color = GoldPrimary); TextField(value = tileLabelSelect, onValueChange = { tileLabelSelect = it }, colors = TextFieldDefaults.colors(focusedContainerColor = ObsidianBlack, unfocusedContainerColor = ObsidianBlack, focusedTextColor = Color.White), modifier = Modifier.fillMaxWidth().height(48.dp)) }
                        item { Text("Assign Arcane Spell Effect:", fontSize = 11.sp, color = GoldPrimary); var actionExpanded by remember { mutableStateOf(false) }; Box(modifier = Modifier.fillMaxWidth()) { Button(onClick = { actionExpanded = true }, colors = ButtonDefaults.buttonColors(containerColor = ObsidianBlack), modifier = Modifier.fillMaxWidth()) { Text(tileActionSelect.displayName, color = Color.White, fontSize = 12.sp) }; DropdownMenu(expanded = actionExpanded, onDismissRequest = { actionExpanded = false }) { TileAction.values().forEach { action -> DropdownMenuItem(text = { Text(action.displayName) }, onClick = { tileActionSelect = action; actionExpanded = false }) } } } }
                        item { Text("Arcane Spell Theme:", fontSize = 11.sp, color = GoldPrimary); var categoryExpanded by remember { mutableStateOf(false) }; Box(modifier = Modifier.fillMaxWidth()) { Button(onClick = { categoryExpanded = true }, colors = ButtonDefaults.buttonColors(containerColor = ObsidianBlack), modifier = Modifier.fillMaxWidth()) { Text(tileCardCategorySelect.displayName, color = Color.White, fontSize = 12.sp) }; DropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) { listOf(TileType.TRUTH, TileType.DARE, TileType.KINKY_LIGHT, TileType.TEASE_DENIAL, TileType.IMPACT_DOM, TileType.GROUP_BDSM).forEach { type -> DropdownMenuItem(text = { Text(type.displayName) }, onClick = { tileCardCategorySelect = type; categoryExpanded = false }) } } } }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(onClick = { viewModel.inactivePillars.add(Tile(index = 100 + viewModel.inactivePillars.size, label = "${tileLabelSelect} (Clone)", action = tileActionSelect, cardCategory = tileCardCategorySelect, moveSpacesValue = tileMoveSpacesValueSelect.toIntOrNull() ?: 2, customMessageText = tileCustomMessageSelect, emoji = "")); showCustomizerDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = DarkSurface), border = BorderStroke(1.dp, GoldPrimary), modifier = Modifier.weight(1f)) { Text("Clone Pillar", color = GoldPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                        if (isCurrentlyActive) {
                            Button(onClick = { if (viewModel.board.size > 4) { viewModel.board.remove(editingTile); val reindexed = ArrayList<Tile>(); viewModel.board.forEachIndexed { idx, tile -> reindexed.add(tile.copy(index = idx)) }; viewModel.board.clear(); viewModel.board.addAll(reindexed); viewModel.inactivePillars.add(editingTile); showCustomizerDialog = false } }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42000A)), border = BorderStroke(1.dp, CrimsonGlow), modifier = Modifier.weight(1f), enabled = viewModel.board.size > 4) { Text("Deactivate", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                        } else {
                            Button(onClick = { viewModel.board.add(editingTile.copy(index = viewModel.board.size)); viewModel.inactivePillars.remove(editingTile); showCustomizerDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = PurpleArcane), border = BorderStroke(1.dp, Color(0xFFD28EFF)), modifier = Modifier.weight(1f)) { Text("Activate", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedButton(onClick = { showCustomizerDialog = false }, border = BorderStroke(1.dp, TextMuted), modifier = Modifier.weight(1.5f)) { Text("Cancel", color = Color.LightGray) }
                        Button(onClick = { if (isCurrentlyActive) { val idx = viewModel.board.indexOfFirst { it.index == editingTile.index }; if (idx != -1) { viewModel.board[idx] = editingTile.copy(label = tileLabelSelect, action = tileActionSelect, cardCategory = tileCardCategorySelect, moveSpacesValue = tileMoveSpacesValueSelect.toIntOrNull() ?: 2, customMessageText = tileCustomMessageSelect, emoji = "") } } else { val idx = viewModel.inactivePillars.indexOfFirst { it.index == editingTile.index }; if (idx != -1) { viewModel.inactivePillars[idx] = editingTile.copy(label = tileLabelSelect, action = tileActionSelect, cardCategory = tileCardCategorySelect, moveSpacesValue = tileMoveSpacesValueSelect.toIntOrNull() ?: 2, customMessageText = tileCustomMessageSelect, emoji = "") } }; showCustomizerDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = CrimsonGlow), modifier = Modifier.weight(2f)) { Text("Save Pillar", color = Color.White, fontWeight = FontWeight.Black) }
                    }
                }
            }
        }
    }
}

private fun getSigilVariant(type: TileType, action: TileAction): String {
    return when (action) {
        TileAction.PUNISHMENT -> "PUNISHMENT"; TileAction.SKIP_TURN -> "SKIP_TURN"; TileAction.MOVE_SPACES -> "MOVE_SPACES"; TileAction.SWAP_POS -> "SWAP_POS"; TileAction.ROLL_AGAIN -> "ROLL_AGAIN"; TileAction.BOARD_SHUFFLE -> "BOARD_SHUFFLE"; TileAction.DOUBLE_DARE -> "DOUBLE_DARE"; TileAction.CUSTOM_MESSAGE -> "CUSTOM_MESSAGE"
        else -> when (type) { TileType.START -> "START"; TileType.TRUTH -> "TRUTH"; TileType.DARE -> "DARE"; TileType.KINKY_LIGHT -> "KINKY_LIGHT"; TileType.TEASE_DENIAL -> "TEASE_DENIAL"; TileType.IMPACT_DOM -> "IMPACT_DOM"; TileType.GROUP_BDSM -> "GROUP_BDSM"; else -> "NORMAL_CARD" }
    }
}
