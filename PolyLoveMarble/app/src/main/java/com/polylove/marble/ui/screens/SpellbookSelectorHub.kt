package com.polylove.marble.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.polylove.marble.game.PromptDatabase
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.components.drawPadlockVector
import com.polylove.marble.ui.theme.*

@Composable
fun SpellbookSelectorHub(
    viewModel: GameViewModel,
    hapticFeedback: HapticFeedback,
    onSpellbookSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDeckDialog by remember { mutableStateOf(false) }
    var newDeckNameText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CHOOSE A SPELL BOOK TO WEAVE",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Mandatory Selection: Choose or engrave a spell book deck folder to begin editing.",
            fontSize = 10.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Prominent "+ CREATE NEW DECK" Button
        Button(
            onClick = { 
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                showCreateDeckDialog = true 
            },
            colors = ButtonDefaults.buttonColors(containerColor = CrimsonGlow),
            border = BorderStroke(1.5.dp, GoldPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Deck", tint = GoldPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "CREATE NEW SPELL BOOK",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            gridItems(viewModel.availableSpellbooks) { packName ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    border = BorderStroke(1.5.dp, GoldPrimary.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clickable { 
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            onSpellbookSelected(packName)
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF2B0E1E))
                                .border(1.dp, GoldPrimary, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(16.dp)) {
                                drawPadlockVector(isClosed = true, color = GoldPrimary)
                            }
                        }
                        
                        Text(
                            text = packName.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        val packPromptCount = PromptDatabase.prompts.count { it.packName == packName }
                        val packPunishCount = PromptDatabase.customPunishments.count { it.packName == packName }
                        Text(
                            text = "${packPromptCount} Spells • ${packPunishCount} Curses",
                            fontSize = 8.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (showCreateDeckDialog) {
        Dialog(onDismissRequest = { showCreateDeckDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                border = BorderStroke(1.5.dp, GoldPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ENGRAVE NEW SPELL BOOK",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    TextField(
                        value = newDeckNameText,
                        onValueChange = { newDeckNameText = it },
                        placeholder = { Text("e.g. Whispers of Silk") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = ObsidianBlack,
                            unfocusedContainerColor = ObsidianBlack,
                            focusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showCreateDeckDialog = false },
                            border = BorderStroke(1.dp, TextMuted),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = Color.LightGray)
                        }
                        
                        Button(
                            onClick = {
                                if (newDeckNameText.isNotBlank() && !viewModel.availableSpellbooks.contains(newDeckNameText)) {
                                    viewModel.availableSpellbooks.add(newDeckNameText)
                                    viewModel.selectedSpellbooks.add(newDeckNameText)
                                    onSpellbookSelected(newDeckNameText)
                                    showCreateDeckDialog = false
                                    newDeckNameText = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonGlow),
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Text("Engrave", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
