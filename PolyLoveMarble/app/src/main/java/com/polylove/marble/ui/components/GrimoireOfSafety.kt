package com.polylove.marble.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.ui.theme.BrassGold

@Composable
fun GrimoireOfSafety(
    hapticFeedback: HapticFeedback,
    modifier: Modifier = Modifier
) {
    var showGrimoire by remember { mutableStateOf(false) }
    KinkyCard(borderColor = BrassGold.copy(alpha = 0.5f), modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    showGrimoire = !showGrimoire
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Book, "Grimoire", tint = BrassGold)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Grimoire of Safety & Rules",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrassGold
                )
            }
            Icon(
                if (showGrimoire) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand Rules",
                tint = BrassGold
            )
        }
        
        AnimatedVisibility(visible = showGrimoire) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "The Golden Rule: Consensual & Role-Neutral",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "This session supports 3 to 8 players. Prompts are carefully drafted to remain role-neutral (avoiding pre-assuming who is dominant or submissive). Players negotiate safe-words and boundaries BEFORE beginning.",
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "The Traffic Light Safety Protocol",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "🔴 RED: Stop immediately and cease the action.\n🟡 YELLOW: Slow down, reduce intensity, or modify the action.\n🟢 GREEN: Safe to proceed at the current level or increase intensity.",
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Consent & Comfort Check-ins",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Any player can veto any card or request a substitute task at any time with zero judgment. Tap 'SKIP' on any prompt modal to draw a random curse instead to preserve session pacing.",
                    fontSize = 10.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}
