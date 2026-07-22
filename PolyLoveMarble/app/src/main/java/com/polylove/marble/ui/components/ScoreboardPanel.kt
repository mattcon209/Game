package com.polylove.marble.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.game.Player
import com.polylove.marble.ui.GameScreen
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.theme.*

@Composable
fun ScoreboardPanel(
    viewModel: GameViewModel,
    activePlayer: Player,
    hapticFeedback: HapticFeedback,
    modifier: Modifier = Modifier
) {
    // Standings Card - Renders Locked Badges for Bound players! (Visual Tracking!)
    KinkyCard(borderColor = CrimsonGlow.copy(alpha = 0.25f), modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Standings",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary
            )
            
            if (viewModel.isInfiniteMode) {
                Text(
                    text = "END",
                    color = CrimsonGlow,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            val highestTasksWinner = viewModel.players.maxByOrNull { it.tasksCompleted }
                            viewModel.winnerName = highestTasksWinner?.name ?: "No one"
                            viewModel.victorySummaryText = "The play session has concluded. ${viewModel.winnerName} completed the most tasks (${highestTasksWinner?.tasksCompleted})!"
                            viewModel.currentScreen = GameScreen.Win
                        }
                        .border(1.dp, CrimsonGlow, RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(2.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            lazyItems(viewModel.players) { p ->
                val activeRestraintsCount = viewModel.activeConstraints.count { it.playerName == p.name }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (p == activePlayer) activePlayer.color.copy(alpha = 0.08f) else Color.Transparent,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val pIndex = viewModel.players.indexOf(p)
                        OccultPlayerAvatar(
                            playerColor = p.color,
                            playerName = p.name,
                            playerIndex = if (pIndex != -1) pIndex else 0,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = buildString {
                                append(p.name)
                                if (p.isBound) append(" 🚫")
                                if (activeRestraintsCount > 0) append(" ⛓️")
                            },
                            fontSize = 11.sp,
                            fontWeight = if (p == activePlayer) FontWeight.Bold else FontWeight.Normal,
                            color = if (p == activePlayer) p.color else Color.White
                        )
                    }
                    Text(
                        text = "T: ${p.tasksCompleted} | C: ${p.punishmentsTaken}",
                        fontSize = 10.sp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}
