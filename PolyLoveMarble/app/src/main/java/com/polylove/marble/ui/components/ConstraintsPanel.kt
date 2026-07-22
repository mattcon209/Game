package com.polylove.marble.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.KinkySoundManager
import com.polylove.marble.ui.theme.*

@Composable
fun ConstraintsPanel(
    viewModel: GameViewModel,
    hapticFeedback: HapticFeedback,
    soundManager: KinkySoundManager,
    modifier: Modifier = Modifier
) {
    // Active Constraints Card (COVENANTS PANEL)
    KinkyCard(borderColor = CrimsonGlow.copy(alpha = 0.35f), modifier = modifier) {
        Text(
            text = "Active Spell Constraints",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CrimsonGlow,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        if (viewModel.activeConstraints.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No constraints active", fontSize = 9.sp, color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                lazyItems(viewModel.activeConstraints) { constraint ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 1.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Black.copy(alpha = 0.3f))
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = constraint.playerName,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                            Text(
                                text = constraint.description,
                                fontSize = 9.sp,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        if (constraint.durationUnit == "times") {
                            IconButton(
                                onClick = {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    if (constraint.remainingTimes > 1) {
                                        constraint.remainingTimes--
                                    } else {
                                        viewModel.activeConstraints.remove(constraint)
                                        viewModel.constraintAlertTitle = "🔓 RESTRAINT FULFILLED! 🔓"
                                        viewModel.constraintAlertText = "${constraint.playerName} has completed all reps of:\n\n'${constraint.description}'"
                                        viewModel.showConstraintAlert = true
                                        soundManager.play("flip")
                                    }
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, "Done Rep", tint = GoldPrimary, modifier = Modifier.size(16.dp))
                            }
                        } else {
                            val timerText = when (constraint.durationUnit) {
                                "seconds", "minutes" -> {
                                    val mins = constraint.remainingSeconds / 60
                                    val secs = constraint.remainingSeconds % 60
                                    String.format("%02d:%02d", mins, secs)
                                }
                                "turns" -> "${constraint.remainingTurns} turns"
                                else -> ""
                            }
                            Text(
                                text = timerText,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = CrimsonGlow
                            )
                        }
                    }
                }
            }
        }
    }
}
