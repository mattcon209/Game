package com.polylove.marble.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.game.*
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.components.*
import com.polylove.marble.ui.theme.*

@Composable
fun CardEditorTab(viewModel: GameViewModel, activeSpellbookName: String, hapticFeedback: HapticFeedback, modifier: Modifier = Modifier) {
    var activeFocus by remember { mutableStateOf("BUILDER") }

    Column(modifier = modifier.fillMaxSize()) {
        Card(colors = CardDefaults.cardColors(containerColor = DarkCardBg.copy(alpha = 0.6f)), border = BorderStroke(1.5.dp, if (activeFocus == "BUILDER") CrimsonGlow else Color.White.copy(alpha = 0.2f)), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth().clickable { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); activeFocus = "BUILDER" }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔮 SPELL WEAVER / CARD BUILDER", fontSize = 13.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Serif, color = if (activeFocus == "BUILDER") GoldPrimary else TextMuted)
                    Icon(imageVector = if (activeFocus == "BUILDER") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = "Toggle", tint = if (activeFocus == "BUILDER") GoldPrimary else TextMuted)
                }
                AnimatedVisibility(visible = activeFocus == "BUILDER", enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(500.dp)) {
                        StructuredCardBuilder(viewModel = viewModel, activeSpellbook = activeSpellbookName, hapticFeedback = hapticFeedback, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        val filteredPrompts = PromptDatabase.prompts.filter { it.packName == activeSpellbookName }
        Card(colors = CardDefaults.cardColors(containerColor = DarkCardBg.copy(alpha = 0.6f)), border = BorderStroke(1.5.dp, if (activeFocus == "LIBRARY") CrimsonGlow else Color.White.copy(alpha = 0.2f)), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).weight(if (activeFocus == "LIBRARY") 1f else 0.15f, fill = false)) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth().clickable { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); activeFocus = "LIBRARY" }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔏 SPELL LIBRARY & MANAGER (${filteredPrompts.size})", fontSize = 13.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Serif, color = if (activeFocus == "LIBRARY") GoldPrimary else TextMuted)
                    Icon(imageVector = if (activeFocus == "LIBRARY") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = "Toggle", tint = if (activeFocus == "LIBRARY") GoldPrimary else TextMuted)
                }
                AnimatedVisibility(visible = activeFocus == "LIBRARY", enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(400.dp)) {
                        LazyVerticalGrid(columns = GridCells.Fixed(3), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxSize()) {
                            gridItems(filteredPrompts) { card ->
                                val isCurrentlyDisabled = viewModel.disabledPrompts.contains(card)
                                Card(colors = CardDefaults.cardColors(containerColor = if (isCurrentlyDisabled) DarkSurface.copy(alpha = 0.4f) else DarkSurface), border = BorderStroke(1.5.dp, if (isCurrentlyDisabled) Color.White.copy(alpha = 0.2f) else CrimsonGlow.copy(alpha = 0.5f)), modifier = Modifier.fillMaxWidth().height(110.dp).alpha(if (isCurrentlyDisabled) 0.5f else 1f)) {
                                    Column(modifier = Modifier.fillMaxSize().padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                            MagicCircleSigil(variant = getCardSigilVariant(card.category), color = Color(android.graphics.Color.parseColor(card.category.colorHex)), size = 20)
                                            Text(text = if (isCurrentlyDisabled) "LOCKED" else card.spiceLevel.displayName.take(8), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = if (isCurrentlyDisabled) Color.Gray else GoldPrimary)
                                        }
                                        Text(text = card.template, fontSize = 9.sp, color = Color.White, maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center, lineHeight = 11.sp, modifier = Modifier.weight(1f).padding(top = 2.dp))
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                            KinkyPadlockCheckbox(checked = !isCurrentlyDisabled, onCheckedChange = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); if (isCurrentlyDisabled) { viewModel.disabledPrompts.remove(card) } else { viewModel.disabledPrompts.add(card) } }, modifier = Modifier.size(24.dp))
                                            val isDefault = listOf("Base Deck", "Sensory & Tease", "Bondage & Restraints", "Impact Play", "Polyamorous Group Scenes").contains(card.packName)
                                            if (!isDefault) { IconButton(onClick = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); PromptDatabase.prompts.remove(card) }, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Delete, "Delete", tint = CrimsonGlow, modifier = Modifier.size(14.dp)) } }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getCardSigilVariant(category: TileType): String {
    return when (category) { TileType.TRUTH -> "TRUTH"; TileType.DARE -> "DARE"; TileType.KINKY_LIGHT -> "KINKY_LIGHT"; TileType.TEASE_DENIAL -> "TEASE_DENIAL"; TileType.IMPACT_DOM -> "IMPACT_DOM"; TileType.GROUP_BDSM -> "GROUP_BDSM"; else -> "NORMAL_CARD" }
}
