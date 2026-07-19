package com.polylove.marble.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.game.*
import com.polylove.marble.ui.GameScreen
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.components.SeductiveLeatherBackground
import com.polylove.marble.ui.theme.*

@Composable
fun EditorScreen(viewModel: GameViewModel) {
    var editorTab by remember { mutableStateOf("TILES") } // Default to TILES
    var activeSpellbook by remember { mutableStateOf<String?>(null) } // null = Selector Hub
    val hapticFeedback = LocalHapticFeedback.current

    SeductiveLeatherBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Row with Back Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { 
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (activeSpellbook != null) {
                        activeSpellbook = null // Back arrow inside book tabs goes back to the Selector Hub first! (PRESERVING PROGRESS)
                    } else {
                        viewModel.currentScreen = GameScreen.Setup // Back from Selector Hub exits back to the Setup lobby!
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = LatexCrimson)
                }
                Text(
                    text = "CREATION WORKSHOP",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = BrassGold
                )
                Spacer(modifier = Modifier.width(40.dp))
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            if (activeSpellbook == null) {
                // ==========================================
                // 1. MANDATORY SPELLBOOK SELECTOR HUB
                // ==========================================
                SpellbookSelectorHub(
                    viewModel = viewModel,
                    hapticFeedback = hapticFeedback,
                    onSpellbookSelected = { activeSpellbook = it },
                    modifier = Modifier.weight(1f)
                )
            } else {
                // ==========================================
                // 2. NOW VIEWING TABS FOR SELECTED SPELL BOOK
                // ==========================================
                val activeSpellbookName = activeSpellbook!!
                
                // Show current active spell book at top with Change option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(LeatherDarkPurple)
                        .border(1.dp, BrassGold.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ACTIVE SPELL BOOK",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrassGold
                        )
                        Text(
                            text = activeSpellbookName.uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "CHANGE SPELL BOOK",
                        color = LatexCrimson,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier
                            .clickable {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                activeSpellbook = null
                            }
                            .border(1.dp, LatexCrimson, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
                
                // Tab Swappers Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("TILES", "CARDS", "CURSES").forEach { tab ->
                        val isSelected = editorTab == tab
                        Button(
                            onClick = { 
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                editorTab = tab 
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) SeductiveViolet else LeatherDarkPurple
                            ),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(tab, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Box(modifier = Modifier.weight(1f)) {
                    when (editorTab) {
                        "TILES" -> {
                            TileEditorTab(
                                viewModel = viewModel,
                                hapticFeedback = hapticFeedback,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        
                        "CARDS" -> {
                            CardEditorTab(
                                viewModel = viewModel,
                                activeSpellbookName = activeSpellbookName,
                                hapticFeedback = hapticFeedback,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        
                        "CURSES" -> {
                            CurseEditorTab(
                                viewModel = viewModel,
                                activeSpellbookName = activeSpellbookName,
                                hapticFeedback = hapticFeedback,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
