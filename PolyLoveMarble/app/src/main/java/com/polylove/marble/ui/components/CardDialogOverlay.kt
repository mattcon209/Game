package com.polylove.marble.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.polylove.marble.game.*
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.KinkySoundManager
import com.polylove.marble.ui.theme.*
import kotlin.random.Random

// --- CARDS & DIALOGS OVERLAYS (WITH TACTILE 3D CARD FLIP!) ---
@Composable
fun CardDialogOverlay(viewModel: GameViewModel) {
    val activePlayer = viewModel.players[viewModel.currentPlayerIndex]
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current
    val soundManager = remember { KinkySoundManager(context) }

    if (viewModel.showCardDialog) {
        Dialog(onDismissRequest = { }) {
            var isCardFlipped by remember { mutableStateOf(false) }
            val cardRotation by animateFloatAsState(
                targetValue = if (isCardFlipped) 180f else 0f,
                animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
                label = "CardFlipAnimation"
            )
            
            val borderThemeColor = when (viewModel.cardDialogType) {
                "PUNISHMENT" -> Color(0xFFFF007F)
                "MOTIF" -> GoldPrimary
                "DOUBLE_CHALLENGE" -> CrimsonGlow
                else -> when (viewModel.currentPromptCategory) {
                    TileType.START -> CrimsonGlow
                    TileType.TRUTH -> Color.White
                    TileType.DARE -> CrimsonGlow
                    TileType.KINKY_LIGHT -> CrimsonGlow
                    TileType.TEASE_DENIAL -> TextPrimary
                    TileType.IMPACT_DOM -> CrimsonGlow
                    TileType.GROUP_BDSM -> Color(0xFF00FFCC)
                    else -> CrimsonGlow
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        rotationY = cardRotation
                        cameraDistance = 12f * density
                    }
                    .clickable {
                        if (!isCardFlipped) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            isCardFlipped = true
                        }
                    }
            ) {
                // RENDER CARD BACK SIDE
                if (cardRotation <= 90f) {
                    KinkyCard(borderColor = borderThemeColor) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(modifier = Modifier.size(54.dp)) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawPadlockVector(isClosed = true, color = borderThemeColor)
                                }
                            }
                            Spacer(modifier = Modifier.height(18.dp))
                            Text(
                                text = "KINK CARD DRAWN",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldPrimary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Tap Card Back to flip and reveal bounds...",
                                fontSize = 11.sp,
                                color = Color.LightGray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                } else {
                    // RENDER CARD FRONT SIDE
                    KinkyCard(
                        borderColor = borderThemeColor,
                        modifier = Modifier.graphicsLayer { rotationY = 180f }
                    ) {
                        Box(modifier = Modifier.size(36.dp)) {
                            TileVectorIcon(type = viewModel.currentPromptCategory, action = if (viewModel.cardDialogType == "PUNISHMENT") TileAction.PUNISHMENT else TileAction.NORMAL_CARD, modifier = Modifier.fillMaxSize())
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (viewModel.cardDialogType) {
                                "PUNISHMENT" -> "CURSE CARD"
                                "MOTIF" -> viewModel.motifTitle
                                "DOUBLE_CHALLENGE" -> viewModel.motifTitle
                                else -> viewModel.currentPromptCategory.displayName.uppercase()
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = borderThemeColor
                        )
                        
                        Text(
                            text = if (viewModel.cardDialogType == "PUNISHMENT") "A task was skipped/refused. Endure your curse!" else "Deck Theme: ${viewModel.currentPromptPack} | ${viewModel.currentPromptSpice.displayName}",
                            fontSize = 9.sp,
                            color = Color.LightGray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        HorizontalDivider(color = CrimsonGlow.copy(alpha = 0.25f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(14.dp))
                        
                        Text(
                            text = viewModel.currentPromptText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (viewModel.cardDialogType == "PROMPT" || viewModel.cardDialogType == "DOUBLE_CHALLENGE") {
                                OutlinedButton(
                                    onClick = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.cardDialogType = "PUNISHMENT"
                                        val rawList = PromptDatabase.getPunishmentsForSpellbook(viewModel.activeSpellbook ?: "Base Spellbook")
                                        val activeList = rawList.filter { it !in viewModel.disabledPunishments }
                                        val rawPunishment = if (activeList.isNotEmpty()) activeList.random() else "Perform 5 submissive squats."
                                        viewModel.currentPromptText = PromptResolver.resolve(rawPunishment, activePlayer, viewModel.players.toList())
                                        viewModel.currentPromptCategory = TileType.PUNISHMENT
                                        activePlayer.punishmentsTaken += 1
                                        viewModel.isDoubleChallenge = false
                                        
                                        // Reset prompt duration so refusing a prompt doesn't register constraint
                                        viewModel.currentPromptDurationValue = 0
                                        viewModel.currentPromptDurationUnit = ""
                                    },
                                    border = BorderStroke(1.dp, Color(0xFFFF007F)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF007F)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Refuse 💢", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                
                                Button(
                                    onClick = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        
                                        // TRIGGER CONSTRAINTS REGISTER UPON SUCCESSFUL TASK COMPLETION
                                        if (viewModel.currentPromptDurationValue > 0) {
                                            val newConstraint = ActiveConstraint(
                                                id = Random.nextInt(100000).toString(),
                                                playerName = activePlayer.name,
                                                description = viewModel.currentPromptText,
                                                durationValue = viewModel.currentPromptDurationValue,
                                                durationUnit = viewModel.currentPromptDurationUnit,
                                                remainingSeconds = if (viewModel.currentPromptDurationUnit == "seconds") viewModel.currentPromptDurationValue else if (viewModel.currentPromptDurationUnit == "minutes") viewModel.currentPromptDurationValue * 60 else 0,
                                                remainingTurns = if (viewModel.currentPromptDurationUnit == "turns") viewModel.currentPromptDurationValue else 0,
                                                remainingTimes = if (viewModel.currentPromptDurationUnit == "times") viewModel.currentPromptDurationValue else 0
                                            )
                                            viewModel.activeConstraints.add(newConstraint)
                                        }

                                        // Decrement turns constraints when players conclude tasks
                                        val expiredTurns = ArrayList<ActiveConstraint>()
                                        viewModel.activeConstraints.forEach { constraint ->
                                            if (constraint.durationUnit == "turns") {
                                                if (constraint.remainingTurns > 0) {
                                                    constraint.remainingTurns--
                                                    if (constraint.remainingTurns == 0) {
                                                        expiredTurns.add(constraint)
                                                    }
                                                }
                                            }
                                        }
                                        if (expiredTurns.isNotEmpty()) {
                                            expiredTurns.forEach { constraint ->
                                                viewModel.activeConstraints.remove(constraint)
                                                viewModel.constraintAlertTitle = "⛓️ BOUND RELEASED! 🔓"
                                                viewModel.constraintAlertText = "${constraint.playerName}'s turn-duration constraint has concluded! They are released from:\n\n'${constraint.description}'"
                                                viewModel.showConstraintAlert = true
                                                soundManager.play("shuffle")
                                            }
                                        }

                                        if (viewModel.isDoubleChallenge && viewModel.doubleChallengeStage == 1) {
                                            viewModel.doubleChallengeStage = 2
                                            viewModel.triggerNextTurnOnClose = false
                                            
                                            viewModel.handleTileActionTrigger(activePlayer, Tile(1000, "Double Challenge", TileAction.NORMAL_CARD, viewModel.doubleChallengeCategory), soundManager)
                                            isCardFlipped = false
                                        } else {
                                            activePlayer.tasksCompleted += if (viewModel.isDoubleChallenge) 2 else 1
                                            viewModel.showCardDialog = false
                                            viewModel.isDoubleChallenge = false
                                            viewModel.currentPlayerIndex = (viewModel.currentPlayerIndex + 1) % viewModel.players.size
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonGlow),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1.2f)
                                ) {
                                    Text(
                                        text = if (viewModel.isDoubleChallenge && viewModel.doubleChallengeStage == 1) "Card 1 Done ✨" else "Complete ✨",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            } else {
                                Button(
                                    onClick = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.showCardDialog = false
                                        if (viewModel.triggerNextTurnOnClose) {
                                            viewModel.currentPlayerIndex = (viewModel.currentPlayerIndex + 1) % viewModel.players.size
                                        }
                                        
                                        // Turn Advanced: decrement any turns constraints
                                        val expiredTurns = ArrayList<ActiveConstraint>()
                                        viewModel.activeConstraints.forEach { constraint ->
                                            if (constraint.durationUnit == "turns") {
                                                if (constraint.remainingTurns > 0) {
                                                    constraint.remainingTurns--
                                                    if (constraint.remainingTurns == 0) {
                                                        expiredTurns.add(constraint)
                                                    }
                                                }
                                            }
                                        }
                                        if (expiredTurns.isNotEmpty()) {
                                            expiredTurns.forEach { constraint ->
                                                viewModel.activeConstraints.remove(constraint)
                                                viewModel.constraintAlertTitle = "⛓️ BOUND RELEASED! 🔓"
                                                viewModel.constraintAlertText = "${constraint.playerName}'s turn-duration constraint has concluded! They are released from:\n\n'${constraint.description}'"
                                                viewModel.showConstraintAlert = true
                                                soundManager.play("shuffle")
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonGlow),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Accept & Continue ⛓️", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
