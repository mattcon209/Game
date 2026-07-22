package com.polylove.marble.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.polylove.marble.game.*
import com.polylove.marble.ui.EmberParticle
import com.polylove.marble.ui.GameScreen
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.KinkySoundManager
import com.polylove.marble.ui.components.*
import com.polylove.marble.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun BoardScreen(viewModel: GameViewModel) {
    val context = LocalContext.current
    val soundManager = remember { KinkySoundManager(context) }
    val coroutineScope = rememberCoroutineScope()
    val activePlayer = viewModel.players[viewModel.currentPlayerIndex]
    val hapticFeedback = LocalHapticFeedback.current
    val density = LocalDensity.current.density
    
    val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    // Cinematic camera and Turn-Start prompt states
    var showTurnStartPrompt by remember { mutableStateOf(true) }
    var pendingLandingTile by remember { mutableStateOf<Tile?>(null) } // POP-UP LANDING STATE!
    
    // Automatically reveal turn-start prompt on player turn change
    LaunchedEffect(viewModel.currentPlayerIndex) {
        showTurnStartPrompt = true
    }

    // Determine camera focus tile index
    val focusTileIndex = if (viewModel.animatedPlayerIndex != -1) {
        viewModel.animatedPlayerTargetTile // Follow meeple as it leaps!
    } else {
        activePlayer.position
    }

    // Determine target zoom: 1x on Turn-Start (zoomed out), 1.9x during zoom follow
    val targetZoom = if (showTurnStartPrompt) 1.0f else 1.25f

    // Calculate target focal coordinates percentages based on focus cell index
    val (col, row) = BoardCreator.getGridCoordinates(focusTileIndex, viewModel.gridSize)
    val cellCenterPercentX = (col + 0.5f) / viewModel.gridSize
    val cellCenterPercentY = (row + 0.5f) / viewModel.gridSize
    
    // Center is 0.5f. Calculate pan percentage displacement
    val targetPanXPercent = if (showTurnStartPrompt) 0f else (0.5f - cellCenterPercentX) * targetZoom
    val targetPanYPercent = if (showTurnStartPrompt) 0f else (0.5f - cellCenterPercentY) * targetZoom

    // Animate zoom and pan floats smoothly
    val animZoom by animateFloatAsState(
        targetValue = targetZoom,
        animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
        label = "CinematicZoom"
    )
    val animPanXPercent by animateFloatAsState(
        targetValue = targetPanXPercent,
        animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
        label = "CinematicPanX"
    )
    val animPanYPercent by animateFloatAsState(
        targetValue = targetPanYPercent,
        animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
        label = "CinematicPanY"
    )

    // Accelerometer tilt floating sways (3D Parallax!)
    var tiltX by remember { mutableStateOf(0f) }
    var tiltY by remember { mutableStateOf(0f) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    tiltX = tiltX * 0.85f + event.values[0] * 0.15f // Yaw tilt
                    tiltY = tiltY * 0.85f + event.values[1] * 0.15f // Pitch tilt
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        if (accelerometer != null) {
            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        }
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    // Background drifting ash/embers particle engine simulation (Juice!)
    val particles = remember { mutableStateListOf<EmberParticle>() }
    LaunchedEffect(Unit) {
        repeat(15) {
            particles.add(EmberParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                vx = (Random.nextFloat() - 0.5f) * 0.02f,
                vy = -Random.nextFloat() * 0.05f - 0.02f,
                size = Random.nextFloat() * 3f + 1.5f,
                alpha = Random.nextFloat() * 0.4f + 0.1f,
                life = Random.nextFloat() * 100f,
                maxLife = Random.nextFloat() * 150f + 100f
            ))
        }
        while (true) {
            delay(33)
            particles.forEachIndexed { idx, p ->
                p.x += p.vx
                p.y += p.vy
                p.life += 1f
                if (p.life >= p.maxLife || p.y < 0f || p.x < 0f || p.x > 1f) {
                    particles[idx] = EmberParticle(
                        x = Random.nextFloat(),
                        y = 1.05f,
                        vx = (Random.nextFloat() - 0.5f) * 0.02f,
                        vy = -Random.nextFloat() * 0.05f - 0.02f,
                        size = Random.nextFloat() * 3f + 1.5f,
                        alpha = Random.nextFloat() * 0.4f + 0.1f,
                        life = 0f,
                        maxLife = Random.nextFloat() * 150f + 100f
                    )
                }
            }
        }
    }

    LaunchedEffect(activePlayer.id) {
        if (activePlayer.isBound && !viewModel.isRolling && !viewModel.isTokenMoving && !viewModel.showCardDialog) {
            viewModel.cardDialogType = "MOTIF"
            viewModel.motifTitle = "BOUND IN SHACKLES! 🚫"
            viewModel.currentPromptText = "${activePlayer.name} is bound! This turn is skipped so they can enjoy their restraints."
            viewModel.currentPromptCategory = TileType.SKIP_TURN
            activePlayer.isBound = false
            viewModel.triggerNextTurnOnClose = true
            viewModel.showCardDialog = true
        }
    }

    // Apply Screen shake offset values locally (Visual Juice!)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = viewModel.shakeX.dp, y = viewModel.shakeY.dp)
    ) {
        SeductiveLeatherBackground {
            // Draw floating ambient embers in background
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                particles.forEach { p ->
                    val px = p.x * w
                    val py = p.y * h
                    val curAlpha = p.alpha * (1f - (p.life / p.maxLife))
                    drawCircle(
                        color = Color(0xFFFF007F).copy(alpha = curAlpha.coerceIn(0f, 1f)),
                        radius = p.size * density,
                        center = Offset(px, py)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Turn Info Header
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, activePlayer.color.copy(alpha = 0.8f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .shadow(4.dp, RoundedCornerShape(12.dp), ambientColor = activePlayer.color)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "It's ${activePlayer.name}'s turn!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = activePlayer.color
                            )
                            Text(
                                text = if (viewModel.isInfiniteMode) {
                                    "Infinite Session | Tasks: ${activePlayer.tasksCompleted} | Curses: ${activePlayer.punishmentsTaken} 💢"
                                } else {
                                    "Lap: ${activePlayer.lapsCompleted + 1}/${viewModel.targetLaps} | Tasks: ${activePlayer.tasksCompleted} | Curses: ${activePlayer.punishmentsTaken}"
                                },
                                fontSize = 11.sp,
                                color = Color.LightGray
                             )
                        }
                        // Animated 3D Mini-Pawn Preview with bat wings!
                        Box(modifier = Modifier.size(24.dp)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawGamePawn(color = activePlayer.color, x = size.width/2, y = size.height/2, size = 18f * density)
                            }
                        }
                    }
                }
                
                // Main Board Area (Responsive scaling with Camera viewport!)
                BoxWithConstraints(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxWidth()
                        .aspectRatio(1.0f)
                        .padding(2.dp)
                ) {
                    val totalWidth = maxWidth
                    val cellSize = totalWidth / viewModel.gridSize
                    val totalWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
                    
                    // Convert animated pan percentages to screen translation pixels
                    val animPanX = animPanXPercent * totalWidthPx
                    val animPanY = animPanYPercent * totalWidthPx

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.5.dp, CrimsonGlow.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        // CINEMATIC GRAPHICS VIEWPORT wrapper
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    scaleX = animZoom
                                    scaleY = animZoom
                                    translationX = animPanX
                                    translationY = animPanY
                                }
                        ) {
                            // Render 2.5D Isometric Columns with chosen theme skin!
                            viewModel.board.forEach { tile ->
                                val (col, row) = BoardCreator.getGridCoordinates(tile.index, viewModel.gridSize)
                                val leftOffset = cellSize * col
                                val topOffset = cellSize * row
                                
                                val playersOnThisTile = viewModel.players.filter { it.position == tile.index && viewModel.animatedPlayerIndex != it.id }
                                val isActivePlayerOnThisTile = playersOnThisTile.any { it.id == activePlayer.id }
                                
                                Box(
                                    modifier = Modifier
                                        .size(cellSize)
                                        .offset(x = leftOffset, y = topOffset)
                                        .padding(1.dp)
                                ) {
                                    SummoningPillar(
                                        tile = tile,
                                        playersOnThisTile = playersOnThisTile,
                                        isActivePlayerOnThisTile = isActivePlayerOnThisTile,
                                        pulseScale = pulseScale,
                                        tiltX = tiltX,
                                        tiltY = tiltY,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    
                                    // 3D glossy game pawns/meeples stand centered and OFFSET UPWARDS TO PREVENT SQUISHING!
                                    if (playersOnThisTile.isNotEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .offset(y = cellSize * -0.75f), // Offset up so meeple base sits exactly on the 3x scaled column caps!
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.wrapContentSize()
                                            ) {
                                                playersOnThisTile.forEach { playerOnTile ->
                                                    Box(
                                                        modifier = Modifier
                                                            .size(if (viewModel.gridSize == 6) 30.dp else 36.dp)
                                                            .padding(horizontal = 0.5.dp)
                                                    ) {
                                                        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                                            drawGamePawn(
                                                                color = playerOnTile.color,
                                                                x = size.width / 2,
                                                                y = size.height / 2,
                                                                size = (if (viewModel.gridSize == 6) 24f else 28f) * density
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    
                                    // Meeple active leaps rendering path
                                    if (viewModel.animatedPlayerIndex != -1 && tile.index == viewModel.animatedPlayerTargetTile) {
                                        val activeLeapingPlayer = viewModel.players.firstOrNull { it.id == viewModel.animatedPlayerIndex }
                                        if (activeLeapingPlayer != null) {
                                            // Compute jumping arc offsets
                                            val progress = viewModel.animatedPlayerStepProgress
                                            val arcY = -kotlin.math.sin(progress * Math.PI).toFloat() * 32f * density
                                            
                                            // Find source offset to slide from
                                            val (srcCol, srcRow) = BoardCreator.getGridCoordinates(viewModel.animatedPlayerSourceTile, viewModel.gridSize)
                                            val srcLeft = cellSize * srcCol
                                            val srcTop = cellSize * srcRow
                                            
                                            val cellSizePx = with(LocalDensity.current) { cellSize.toPx() }
                                            val currentLeapX = srcLeft + (leftOffset - srcLeft) * progress
                                            val currentLeapY = srcTop + (topOffset - srcTop) * progress + arcY - (cellSizePx * 0.75f)
                                            
                                            // Squish and stretch meeple base keyframes
                                            val stretchY = 1.0f + (kotlin.math.sin(progress * Math.PI).toFloat() * 0.16f)
                                            val squashX = 1.0f - (kotlin.math.sin(progress * Math.PI).toFloat() * 0.16f)
                                            
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .offset(y = currentLeapY - topOffset),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Box(
                                                    modifier = Modifier.size(if (viewModel.gridSize == 6) 30.dp else 36.dp)
                                                ) {
                                                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                                        drawGamePawn(
                                                            color = activeLeapingPlayer.color,
                                                            x = size.width / 2,
                                                            y = size.height / 2,
                                                            size = (if (viewModel.gridSize == 6) 24f else 28f) * density,
                                                            squashX = squashX,
                                                            stretchY = stretchY
                                                        )
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

                // Middle Active Controls Bar (Dice & Roll Button)
                if (!showTurnStartPrompt) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCardBg.copy(alpha = 0.9f)),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, CrimsonGlow.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // 3D Shaded Double Dice (2d6 Mechanics!)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.scale(if (viewModel.isRolling) pulseScale else 1f)
                            ) {
                                DiceFace3D(value = viewModel.diceValue1, size = 38)
                                DiceFace3D(value = viewModel.diceValue2, size = 38)
                            }
                            
                            // Movement Status Text
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                if (viewModel.isTokenMoving) {
                                    Text(
                                        text = viewModel.animationStatusText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                } else {
                                    Text(
                                        text = "Dominance Value: ${viewModel.diceValue1 + viewModel.diceValue2}",
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            
                            // Roll Button
                            Button(
                                onClick = {
                                    if (!viewModel.isRolling && !viewModel.isTokenMoving) {
                                        coroutineScope.launch {
                                            viewModel.isRolling = true
                                            repeat(12) { i ->
                                                viewModel.diceValue1 = Random.nextInt(1, 7)
                                                viewModel.diceValue2 = Random.nextInt(1, 7)
                                                delay(60 + (i * 15).toLong())
                                            }
                                            val finalRoll1 = Random.nextInt(1, 7)
                                            val finalRoll2 = Random.nextInt(1, 7)
                                            viewModel.diceValue1 = finalRoll1
                                            viewModel.diceValue2 = finalRoll2
                                            val finalRoll = finalRoll1 + finalRoll2
                                            viewModel.isRolling = false
                                            
                                            // Step-by-Step walk animation!
                                            viewModel.isTokenMoving = true
                                            val oldPos = activePlayer.position
                                            val boardSize = viewModel.board.size
                                            
                                            for (step in 1..finalRoll) {
                                                val duration = (350 / viewModel.gameSpeedMultiplier).toLong()
                                                viewModel.animationStatusText = "Moving: $step of $finalRoll ⛓️"
                                                val src = activePlayer.position
                                                val tgt = (src + 1) % boardSize
                                                
                                                viewModel.animatedPlayerIndex = activePlayer.id
                                                viewModel.animatedPlayerSourceTile = src
                                                viewModel.animatedPlayerTargetTile = tgt
                                                
                                                val startTime = System.currentTimeMillis()
                                                while (System.currentTimeMillis() - startTime < duration) {
                                                    val elapsed = System.currentTimeMillis() - startTime
                                                    viewModel.animatedPlayerStepProgress = (elapsed.toFloat() / duration).coerceIn(0f, 1f)
                                                    delay(16) // ~60fps rendering frame rate ticks!
                                                }
                                                viewModel.animatedPlayerStepProgress = 1f
                                                delay(50) // settle pause
                                                activePlayer.position = tgt
                                            }
                                            
                                            viewModel.animatedPlayerIndex = -1
                                            viewModel.animationStatusText = "Arrived! 🔒"
                                            
                                            viewModel.tileLandingCounts[activePlayer.position] = (viewModel.tileLandingCounts[activePlayer.position] ?: 0) + 1
                                            viewModel.shakeX = (Random.nextFloat() - 0.5f) * 12f
                                            viewModel.shakeY = (Random.nextFloat() - 0.5f) * 12f
                                            delay(80)
                                            viewModel.shakeX = 0f
                                            viewModel.shakeY = 0f
                                            
                                            delay(720)
                                            viewModel.isTokenMoving = false
                                            
                                            val newPos = activePlayer.position
                                            if (newPos < oldPos) {
                                                activePlayer.lapsCompleted += 1
                                            }
                                            
                                            if (!viewModel.isInfiniteMode && activePlayer.lapsCompleted >= viewModel.targetLaps) {
                                                viewModel.winnerName = activePlayer.name
                                                viewModel.victorySummaryText = "${activePlayer.name} has crossed the threshold first and asserted absolute dominance!"
                                                viewModel.currentScreen = GameScreen.Win
                                                return@launch
                                            }
                                            
                                            val tile = viewModel.board[newPos]
                                            pendingLandingTile = tile // SHOW LANDING POP-UP ALERT FIRST!
                                        }
                                    }
                                },
                                enabled = !viewModel.isRolling && !viewModel.isTokenMoving,
                                colors = ButtonDefaults.buttonColors(containerColor = activePlayer.color),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .height(34.dp)
                                    .shadow(2.dp, RoundedCornerShape(10.dp))
                            ) {
                                Text(
                                    text = "ROLL",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                
                // Bottom Scoreboard Area (Cleanly extracted into separate modular panels!)
                Row(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ScoreboardPanel(
                        viewModel = viewModel,
                        activePlayer = activePlayer,
                        hapticFeedback = hapticFeedback,
                        modifier = Modifier.weight(1f)
                    )

                    ConstraintsPanel(
                        viewModel = viewModel,
                        hapticFeedback = hapticFeedback,
                        soundManager = soundManager,
                        modifier = Modifier.weight(1.2f)
                    )
                }
            }
        }
    }

    // --- 💎 POP-UP LANDING ALERT OVERLAY ---
    if (pendingLandingTile != null) {
        val tile = pendingLandingTile!!
        Dialog(onDismissRequest = { }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                border = BorderStroke(2.dp, activePlayer.color),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp), ambientColor = activePlayer.color)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black)
                            .border(1.5.dp, activePlayer.color, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        TileVectorIcon(type = tile.cardCategory, action = tile.action, modifier = Modifier.size(36.dp))
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "RUNE LANDED!",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        letterSpacing = 1.sp
                    )
                    
                    Text(
                        text = tile.label.uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    
                    Text(
                        text = when (tile.action) {
                            TileAction.NORMAL_CARD -> "Prepare to invoke a standard ${tile.cardCategory.displayName} Spell."
                            TileAction.DOUBLE_DARE -> "Double Spell! Prepare to face back-to-back challenges!"
                            TileAction.PUNISHMENT -> "Direct Curse Spell! Prepare to endure a randomized penalty!"
                            TileAction.SKIP_TURN -> "Binding Shackle Spell! Your next turn is skipped."
                            TileAction.MOVE_SPACES -> "Whip Lash Displacement Spell! Prepare to move forward or backward."
                            TileAction.SWAP_POS -> "Collar Swap Spell! Exchange places with a random partner."
                            TileAction.ROLL_AGAIN -> "Double Roll Blessing Spell! Granting you another immediate roll."
                            TileAction.BOARD_SHUFFLE -> "Chaos Reshuffle Curse! The summoning circle reshapes."
                            TileAction.CUSTOM_MESSAGE -> "Custom Law Command! Prepare to fulfill the custom law: '${tile.customMessageText}'"
                        },
                        fontSize = 11.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            val finalTile = tile
                            pendingLandingTile = null // close pop-up
                            
                            // Execute actual tile action trigger
                            if (finalTile.action == TileAction.NORMAL_CARD && finalTile.cardCategory == TileType.START) {
                                viewModel.cardDialogType = "MOTIF"
                                viewModel.motifTitle = "SESSION START PASS 🔒"
                                viewModel.currentPromptText = "You successfully navigated the loop. Everyone in the circle must pay you a submissive praise or give you a consensual massage for 30 seconds."
                                viewModel.currentPromptCategory = TileType.START
                                viewModel.triggerNextTurnOnClose = true
                                viewModel.showCardDialog = true
                            } else {
                                viewModel.handleTileActionTrigger(activePlayer, finalTile, soundManager) {
                                    coroutineScope.launch {
                                        delay(400)
                                        viewModel.isTokenMoving = true
                                        val cascadeMoveVal = finalTile.moveSpacesValue
                                        val steps = kotlin.math.abs(cascadeMoveVal)
                                        val direction = if (cascadeMoveVal > 0) 1 else -1
                                        val boardSize = viewModel.board.size
                                        
                                        for (step in 1..steps) {
                                            viewModel.animationStatusText = "Cascade: $step of $steps 🌀"
                                            val src = activePlayer.position
                                            val tgt = (src + direction + boardSize) % boardSize
                                            
                                            viewModel.animatedPlayerIndex = activePlayer.id
                                            viewModel.animatedPlayerSourceTile = src
                                            viewModel.animatedPlayerTargetTile = tgt
                                            
                                            val duration = (350 / viewModel.gameSpeedMultiplier).toLong()
                                            val startTime = System.currentTimeMillis()
                                            while (System.currentTimeMillis() - startTime < duration) {
                                                val elapsed = System.currentTimeMillis() - startTime
                                                viewModel.animatedPlayerStepProgress = (elapsed.toFloat() / duration).coerceIn(0f, 1f)
                                                delay(16)
                                            }
                                            viewModel.animatedPlayerStepProgress = 1f
                                            delay(50)
                                            activePlayer.position = tgt
                                        }
                                        
                                        viewModel.animatedPlayerIndex = -1
                                        delay(800)
                                        viewModel.isTokenMoving = false
                                        val cascadeTile = viewModel.board[activePlayer.position]
                                        viewModel.handleTileActionTrigger(activePlayer, cascadeTile, soundManager)
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = activePlayer.color),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(38.dp)
                    ) {
                        Text("INVOKE SPELL RITUAL", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }

    // Cinematic Turn-Start Overlay Card Dialog
    if (showTurnStartPrompt) {
        Dialog(onDismissRequest = { }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                border = BorderStroke(2.dp, activePlayer.color),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp), ambientColor = activePlayer.color)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SUMMONING PHASE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = activePlayer.name.uppercase(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = activePlayer.color,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Select your intention, center your presence, and prepare to roll the arcane stones to navigate the Dungeon of Desire.",
                        fontSize = 11.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            soundManager.play("roll")
                            showTurnStartPrompt = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = activePlayer.color),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text("BEGIN TURN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
