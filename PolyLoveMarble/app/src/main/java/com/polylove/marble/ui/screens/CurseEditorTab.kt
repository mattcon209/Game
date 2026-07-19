package com.polylove.marble.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.game.*
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.components.KinkyPadlockCheckbox
import com.polylove.marble.ui.components.TileVectorIcon
import com.polylove.marble.ui.theme.*

@Composable
fun CurseEditorTab(
    viewModel: GameViewModel,
    activeSpellbookName: String,
    hapticFeedback: HapticFeedback,
    modifier: Modifier = Modifier
) {
    var curseText by remember { mutableStateOf("") }

    // Split layout: Custom curse builder on top, beautiful collectible Card Grid at bottom!
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Add Custom Booklet Curse",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = SilkPurple
        )
        Spacer(modifier = Modifier.height(4.dp))

        TextField(
            value = curseText,
            onValueChange = { curseText = it },
            placeholder = { Text("e.g. Let {target1} blindfold you for the next two turns...", fontSize = 11.sp) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LeatherDarkPurple,
                unfocusedContainerColor = LeatherDarkPurple,
                focusedTextColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        )
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Button(
            onClick = {
                if (curseText.isNotBlank()) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    PromptDatabase.customPunishments.add(0, BookletPunishment(curseText, activeSpellbookName))
                    curseText = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = LatexCrimson),
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
        ) {
            Text("SAVE CURSE", fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
        
        HorizontalDivider(color = SeductiveViolet.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 4.dp))
        
        val activeCurses = PromptDatabase.getPunishmentsForSpellbook(activeSpellbookName)
        Text(
            text = "${activeSpellbookName} Curses Ledger (${activeCurses.size})", 
            fontSize = 11.sp, 
            color = BrassGold,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // UNIFIED GORGEOUS 2D COLLECTIBLE CARD GRID FORMAT FOR CURSES!
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            gridItems(activeCurses) { p ->
                val isCurrentlyDisabled = viewModel.disabledPunishments.contains(p)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrentlyDisabled) LeatherDarkPurple.copy(alpha = 0.4f) else LeatherDarkPurple
                    ),
                    border = BorderStroke(
                        1.5.dp,
                        if (isCurrentlyDisabled) SteelGrey.copy(alpha = 0.4f) else LatexCrimson.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .alpha(if (isCurrentlyDisabled) 0.5f else 1f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TileVectorIcon(type = TileType.PUNISHMENT, action = TileAction.PUNISHMENT, modifier = Modifier.size(16.dp))
                            Text(
                                text = if (isCurrentlyDisabled) "LOCKED" else "CURSE",
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrentlyDisabled) Color.Gray else Color(0xFFFF007F)
                            )
                        }

                        Text(
                            text = p,
                            fontSize = 9.sp,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            lineHeight = 11.sp,
                            modifier = Modifier.weight(1f).padding(top = 2.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            KinkyPadlockCheckbox(
                                checked = !isCurrentlyDisabled,
                                onCheckedChange = {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    if (isCurrentlyDisabled) {
                                        viewModel.disabledPunishments.remove(p)
                                    } else {
                                        viewModel.disabledPunishments.add(p)
                                    }
                                },
                                modifier = Modifier.size(24.dp)
                            )

                            // Trash bin is shown ONLY if the curse is custom (inside customPunishments)
                            val isCustom = PromptDatabase.customPunishments.any { it.text == p && it.packName == activeSpellbookName }
                            if (isCustom) {
                                IconButton(
                                    onClick = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        val customMatch = PromptDatabase.customPunishments.firstOrNull { it.text == p && it.packName == activeSpellbookName }
                                        if (customMatch != null) {
                                            PromptDatabase.customPunishments.remove(customMatch)
                                        }
                                    },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(Icons.Default.Delete, "Delete", tint = LatexCrimson, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
