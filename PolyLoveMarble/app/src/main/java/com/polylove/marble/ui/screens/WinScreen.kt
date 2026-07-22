package com.polylove.marble.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.game.*
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.components.*
import com.polylove.marble.ui.theme.*

@Composable
fun WinScreen(viewModel: GameViewModel) {
    val winningPlayer = viewModel.players.firstOrNull { it.name == viewModel.winnerName }
    val winningColor = winningPlayer?.color ?: CrimsonGlow
    val density = LocalDensity.current.density
    
    SeductiveLeatherBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "👑 DOMINANCE DECLARED! 👑",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                )
                Text(
                    text = viewModel.victorySummaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = winningColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                )
            }
            
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Standings Card
                item {
                    KinkyCard(borderColor = CrimsonGlow.copy(alpha = 0.5f)) {
                        Text(
                            text = "Final Standings",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        val sortedPlayers = viewModel.players.sortedByDescending { it.tasksCompleted }
                        sortedPlayers.forEachIndexed { rank, p ->
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${rank + 1}.",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (p.name == viewModel.winnerName) GoldPrimary else Color.LightGray,
                                        modifier = Modifier.width(20.dp)
                                    )
                                    Box(modifier = Modifier.size(16.dp)) {
                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                            drawGamePawn(color = p.color, x = size.width/2f, y = size.height/2f, size = 12f * density)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = p.name,
                                        fontSize = 14.sp,
                                        fontWeight = if (p.name == viewModel.winnerName) FontWeight.Bold else FontWeight.Normal,
                                        color = if (p.name == viewModel.winnerName) p.color else Color.White
                                    )
                                }
                                
                                 Text(
                                     text = "Tasks: ${p.tasksCompleted} | Curses: ${p.punishmentsTaken}",
                                     fontSize = 12.sp,
                                     fontWeight = FontWeight.Bold,
                                     color = Color.White
                                 )
                            }
                        }
                    }
                }

                // EXCLUSIVE REQUESTED: Post-Game Heatmap!
                item {
                    KinkyCard(borderColor = CrimsonGlow.copy(alpha = 0.4f)) {
                        Text(
                            text = "Pillars Summoning Heatmap",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonGlow,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Text(
                            text = "Glows brightest on the pillars that players triggered the most this session:",
                            fontSize = 9.sp,
                            color = Color.LightGray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            gridItems(viewModel.board) { tile ->
                                val landings = viewModel.tileLandingCounts[tile.index] ?: 0
                                // Color gets brighter red based on landings
                                val heatColor = if (landings > 0) {
                                    Color(0xFFFF007F).copy(alpha = (0.2f + (landings * 0.25f)).coerceIn(0.2f, 1f))
                                } else {
                                    Color.Gray.copy(alpha = 0.2f)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(heatColor)
                                        .border(1.dp, if (landings > 0) GoldPrimary else Color.Transparent, RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = landings.toString(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (landings > 0) Color.White else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                // EXCLUSIVE REQUESTED: MVP Dominance Badges (Retrospective rewards!)
                item {
                    KinkyCard(borderColor = GoldPrimary.copy(alpha = 0.4f)) {
                        Text(
                            text = "Ritual Dominance Badges",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        
                        val mvpTasks = viewModel.players.maxByOrNull { it.tasksCompleted }
                        val mvpPunish = viewModel.players.maxByOrNull { it.punishmentsTaken }
                        val dodgeSkip = viewModel.players.filter { it.punishmentsTaken == 0 }.randomOrNull() ?: viewModel.players.minBy { it.punishmentsTaken }
                        
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🏆", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("The Prompt Sovereign", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("Granted to ${mvpTasks?.name ?: "No one"} for completing the most card tasks (${mvpTasks?.tasksCompleted ?: 0})!", fontSize = 10.sp, color = Color.LightGray)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💢", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("The Iron Will", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("Granted to ${mvpPunish?.name ?: "No one"} for enduring the most session curses (${mvpPunish?.punishmentsTaken ?: 0})!", fontSize = 10.sp, color = Color.LightGray)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("👻", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("The Unbound Ghost", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("Granted to ${dodgeSkip.name} for successfully evading bounds and skipping penalties!", fontSize = 10.sp, color = Color.LightGray)
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            LeatherStrapButton(
                text = "Close Play Session",
                onClick = { viewModel.resetGame() }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
