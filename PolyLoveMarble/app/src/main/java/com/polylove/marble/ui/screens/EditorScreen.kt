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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.game.*
import com.polylove.marble.ui.GameScreen
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.components.*
import com.polylove.marble.ui.theme.*

@Composable
fun EditorScreen(viewModel: GameViewModel) {
    var editorTab by remember { mutableStateOf("TILES") }
    var activeSpellbook by remember { mutableStateOf<String?>(null) }
    val hapticFeedback = LocalHapticFeedback.current

    SeductiveLeatherBackground {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { 
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (activeSpellbook != null) { activeSpellbook = null } else { viewModel.currentScreen = GameScreen.Setup }
                }) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = CrimsonGlow)
                }
                
                Text(
                    text = "CREATION WORKSHOP",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Serif,
                    color = GoldPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(40.dp))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (activeSpellbook == null) {
                SpellbookSelectorHub(
                    viewModel = viewModel,
                    hapticFeedback = hapticFeedback,
                    onSpellbookSelected = { activeSpellbook = it },
                    modifier = Modifier.weight(1f)
                )
            } else {
                val activeSpellbookName = activeSpellbook!!
                
                GlassPanel(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "ACTIVE SPELL BOOK", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = GoldPrimary, letterSpacing = 1.sp)
                            Text(text = activeSpellbookName.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White, fontFamily = FontFamily.Serif)
                        }
                        Text(
                            text = "CHANGE", color = CrimsonGlow, fontSize = 9.sp, fontWeight = FontWeight.Black,
                            modifier = Modifier.clickable { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); activeSpellbook = null }
                                .border(1.dp, CrimsonGlow, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("TILES", "CARDS", "CURSES").forEach { tab ->
                        val isSelected = editorTab == tab
                        Box(
                            modifier = Modifier.weight(1f).height(36.dp).clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) CrimsonDeep else DarkSurface)
                                .border(1.dp, if (isSelected) CrimsonGlow else Color.White.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .clickable { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress); editorTab = tab },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tab, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else TextMuted, letterSpacing = 1.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(modifier = Modifier.weight(1f)) {
                    when (editorTab) {
                        "TILES" -> TileEditorTab(viewModel = viewModel, hapticFeedback = hapticFeedback, modifier = Modifier.fillMaxSize())
                        "CARDS" -> CardEditorTab(viewModel = viewModel, activeSpellbookName = activeSpellbookName, hapticFeedback = hapticFeedback, modifier = Modifier.fillMaxSize())
                        "CURSES" -> CurseEditorTab(viewModel = viewModel, activeSpellbookName = activeSpellbookName, hapticFeedback = hapticFeedback, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
