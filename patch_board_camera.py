file_path_board = "/home/user/PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/BoardScreen.kt"

with open(file_path_board, "r", encoding="utf-8") as f:
    content = f.read()

content = content.replace("\r\n", "\n")

# Let's completely write the updated, cinematic camera BoardScreen inside BoardScreen.kt
new_board_screen_code = """package com.polylove.marble.ui.screens

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.polylove.marble.game.*
import com.polylove.marble.ui.CryptSkin
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
    val targetZoom = if (showTurnStartPrompt) 1.0f else 1.9f

    // Calculate target focal coordinates percentages based on focus cell index
    val (col, row) = BoardCreator.getGridCoordinates(focusTileIndex, viewModel.gridSize)
    val cellCenterPercentX = (col + 0.5f) / viewModel.gridSize
    val cellCenterPercentY = (row + 0.5f) / viewModel.gridSize
    
    // Center is 0.5f. Calculate pan percentage displacement
    val targetPanXPercent = if (showTurnStartPrompt) 0f else (0.5f - cellCenterPercentX) * targetZoom
    val targetPanYPercent = if (showTurnStartPrompt) 0f else (0.5f - cellCenterPercentY) * targetZoom

    // Animate camera offsets and zoom smoothly
    val animZoom by animateFloatAsState(
        targetValue = targetZoom,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "CameraZoom"
    )
    val animPanXPercent by animateFloatAsState(
        targetValue = targetPanXPercent,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "CameraPanX"
    )
    val animPanYPercent by animateFloatAsState(
        targetValue = targetPanYPercent,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "CameraPanY"
    )

    // Real-Time seconds countdown tick timer engine (ticking seconds and minutes constraints)
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            val expired = ArrayList<ActiveConstraint>()
            viewModel.activeConstraints.forEach { constraint ->
                if (constraint.durationUnit == "seconds" || constraint.durationUnit == "minutes") {
                    if (constraint.remainingSeconds > 0) {
                        constraint.remainingSeconds--
                        if (constraint.remainingSeconds == 0) {
                            expired.add(constraint)
                        }
                    }
                }
            }
            if (expired.isNotEmpty()) {
                expired.forEach { constraint ->
                    viewModel.activeConstraints.remove(constraint)
                    viewModel.constraintAlertTitle = "🕰️ TIME'S UP! 🔓"
                    viewModel.constraintAlertText = "${constraint.playerName} has completed and is released from their constraint:\\n\\n'${constraint.description}'"
                    viewModel.showConstraintAlert = true
                    soundManager.play("shuffle")
                }
            }
        }
    }

    // 3D Gyroscope/Accelerometer Sensor listener (Real-Time tilts columns)
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    var tiltX by remember { mutableStateOf(0f) }
    var tiltY by remember { mutableStateOf(0f) }
    
    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    val ax = event.values[0]
                    val ay = event.values[1]
                    tiltX = tiltX * 0.9f + (ax * 1.2f) * 0.1f
                    tiltY = tiltY * 0.9f + (ay * 1.2f) * 0.1f
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
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
                    colors = CardDefaults.cardColors(containerColor = LeatherDarkPurple),
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
                                    "Infinite Session | Tasks: ${activePlayer.tasksCompleted} | Punishments: ${activePlayer.punishmentsTaken} 💢"
                                } else {
                                    "Lap: ${activePlayer.lapsCompleted + 1}/${viewModel.targetLaps} | Tasks: ${activePlayer.tasksCompleted} | Punishments: ${activePlayer.punishmentsTaken}"
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
                            .border(1.5.dp, SeductiveViolet.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
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
                                        skin = viewModel.selectedSkin,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    
                                    // 3D glossy game pawns/meeples sit centered and OFFSET UPWARDS TO PREVENT SQUISHING!
                                    if (playersOnThisTile.isNotEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .offset(y = (-14).dp), // Offset up so meeple base sits exactly on diamond caps!
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                playersOnThisTile.forEach { p ->
                                                    val pawnSize = if (viewModel.gridSize == 6) 12f else 16f
                                                    Box(modifier = Modifier.size((pawnSize * 2.2f).dp)) { // Larger box prevents clipping heads!
                                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                                            drawGamePawn(color = p.color, x = size.width/2f, y = size.height/2f, size = pawnSize * density)
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.width(1.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            
                            // EXCLUSIVE REQUESTED: HIGH-FIDELITY ACTIVE LEAPING MEEPLE CANVAS OVERLAY!
                            if (viewModel.animatedPlayerIndex != -1) {
                                val density = LocalDensity.current.density
                                val movingPlayer = viewModel.players.firstOrNull { it.id == viewModel.animatedPlayerIndex }
                                if (movingPlayer != null) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val sizeVal = if (viewModel.gridSize == 6) 13f else 17f
                                        val progress = viewModel.animatedPlayerStepProgress
                                        
                                        val (srcCol, srcRow) = BoardCreator.getGridCoordinates(viewModel.animatedPlayerSourceTile, viewModel.gridSize)
                                        val srcX = srcCol * cellSize.toPx() + cellSize.toPx() / 2f
                                        val srcY = srcRow * cellSize.toPx() + cellSize.toPx() / 2f
                                        
                                        val (tgtCol, tgtRow) = BoardCreator.getGridCoordinates(viewModel.animatedPlayerTargetTile, viewModel.gridSize)
                                        val tgtX = tgtCol * cellSize.toPx() + cellSize.toPx() / 2f
                                        val tgtY = tgtRow * cellSize.toPx() + cellSize.toPx() / 2f
                                        
                                        val curX = srcX + (tgtX - srcX) * progress
                                        val curY = srcY + (tgtY - srcY) * progress
                                        
                                        val jumpHeight = -45f * density
                                        val arcY = 4f * progress * (1f - progress) * jumpHeight
                                        val finalY = curY + arcY
                                        
                                        var squashX = 1f
                                        var stretchY = 1f
                                        if (progress < 0.15f) {
                                            stretchY = 1.15f
                                            squashX = 0.85f
                                        } else if (progress > 0.85f) {
                                            stretchY = 0.82f
                                            squashX = 1.18f
                                        }
                                        
                                        // Landing impact shockwave
                                        if (progress >= 0.8f) {
                                            val shockProgress = (progress - 0.8f) / 0.2f
                                            val maxRadius = cellSize.toPx() * 0.45f
                                            val currentRadius = maxRadius * shockProgress
                                            val alpha = 1f - shockProgress
                                            drawCircle(
                                                color = movingPlayer.color.copy(alpha = alpha * 0.8f),
                                                radius = currentRadius,
                                                center = Offset(tgtX, tgtY - (cellSize.toPx() * 0.35f / 2f)),
                                                style = Stroke(width = 3f * density)
                                            )
                                        }
                                        
                                        drawGamePawn(
                                            color = movingPlayer.color,
                                            x = curX,
                                            y = finalY,
                                            size = sizeVal * density,
                                            squashX = squashX,
                                            stretchY = stretchY
                                        )
                                    }
                                }
                            }
                        }

                        // CENTER CONTROLS (Only visible when NOT showing Turn-Start prompt)
                        if (!showTurnStartPrompt) {
                            val centerCells = viewModel.gridSize - 2
                            Box(
                                modifier = Modifier
                                    .size(cellSize * centerCells)
                                    .offset(x = cellSize, y = cellSize)
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    // 3D Shaded Double Dice (2d6 Mechanics!)
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.scale(if (viewModel.isRolling) pulseScale else 1f)
                                    ) {
                                        DiceFace3D(value = viewModel.diceValue1, size = if (viewModel.gridSize == 6) 36 else 44)
                                        DiceFace3D(value = viewModel.diceValue2, size = if (viewModel.gridSize == 6) 36 else 44)
                                    }
                                    
                                    Spacer(modifier = Modifier.height(6.dp))
                                    
                                    AnimatedVisibility(
                                        visible = viewModel.isTokenMoving,
                                        enter = fadeIn(),
                                        exit = fadeOut()
                                    ) {
                                        Text(
                                            text = viewModel.animationStatusText,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SilkPurple,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(4.dp))
                                    
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
                                                    if (tile.action == TileAction.NORMAL_CARD && tile.cardCategory == TileType.START) {
                                                        viewModel.cardDialogType = "MOTIF"
                                                        viewModel.motifTitle = "SESSION START PASS 🔒"
                                                        viewModel.currentPromptText = "You successfully navigated the loop. Everyone in the circle must pay you a submissive praise or give you a consensual massage for 30 seconds."
                                                        viewModel.currentPromptCategory = TileType.START
                                                        viewModel.triggerNextTurnOnClose = true
                                                        viewModel.showCardDialog = true
                                                    } else {
                                                        viewModel.handleTileActionTrigger(activePlayer, tile, soundManager) {
                                                            coroutineScope.launch {
                                                                delay(400)
                                                                viewModel.isTokenMoving = true
                                                                val cascadeMoveVal = tile.moveSpacesValue
                                                                val steps = kotlin.math.abs(cascadeMoveVal)
                                                                val direction = if (cascadeMoveVal > 0) 1 else -1
                                                                
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
                                                                val finalTile = viewModel.board[activePlayer.position]
                                                                viewModel.handleTileActionTrigger(activePlayer, finalTile, soundManager)
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        enabled = !viewModel.isRolling && !viewModel.isTokenMoving,
                                        colors = ButtonDefaults.buttonColors(containerColor = activePlayer.color),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .height(36.dp)
                                            .shadow(4.dp, RoundedCornerShape(12.dp))
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
                    }
                }
                
                // Bottom Scoreboard Area
                Row(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Standings Card - Renders Locked Badges for Bound players! (Visual Tracking!)
                    KinkyCard(borderColor = SeductiveViolet.copy(alpha = 0.25f), modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Standings",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrassGold
                            )
                            
                            if (viewModel.isInfiniteMode) {
                                Text(
                                    text = "END",
                                    color = LatexCrimson,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable {
                                            val highestTasksWinner = viewModel.players.maxByOrNull { it.tasksCompleted }
                                            viewModel.winnerName = highestTasksWinner?.name ?: "No one"
                                            viewModel.victorySummaryText = "The play session has concluded. ${viewModel.winnerName} completed the most tasks (${highestTasksWinner?.tasksCompleted})!"
                                            viewModel.currentScreen = GameScreen.Win
                                        }
                                        .border(1.dp, LatexCrimson, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(2.dp))
                        
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(viewModel.players) { p ->
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
                                        Box(modifier = Modifier.size(12.dp)) {
                                            Canvas(modifier = Modifier.fillMaxSize()) {
                                                drawGamePawn(color = p.color, x = size.width/2, y = size.height/2, size = 10f * density)
                                            }
                                        }
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
                                        text = "T: ${p.tasksCompleted} | P: ${p.punishmentsTaken}",
                                        fontSize = 10.sp,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }
                    }

                    // Active Constraints Card (COVENANTS PANEL)
                    KinkyCard(borderColor = LatexCrimson.copy(alpha = 0.35f), modifier = Modifier.weight(1.2f)) {
                        Text(
                            text = "Active Covenants",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LatexCrimson,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        if (viewModel.activeConstraints.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No constraints active", fontSize = 9.sp, color = Color.Gray)
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(viewModel.activeConstraints) { constraint ->
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
                                                color = BrassGold
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
                                                        viewModel.constraintAlertText = "${constraint.playerName} has completed all reps of:\\n\\n'${constraint.description}'"
                                                        viewModel.showConstraintAlert = true
                                                        soundManager.play("flip")
                                                    }
                                                },
                                                modifier = Modifier.size(20.dp)
                                            ) {
                                                Icon(Icons.Default.CheckCircle, "Done Rep", tint = BrassGold, modifier = Modifier.size(16.dp))
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
                                                color = LatexCrimson
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                Text(
                    text = "Exit to Setup",
                    color = SteelGrey,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { viewModel.resetGame() }
                        .padding(top = 4.dp)
                )
            }
        }
    }

    // Constraint Alarm popup alerts!
    if (viewModel.showConstraintAlert) {
        Dialog(onDismissRequest = { viewModel.showConstraintAlert = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = LeatherDarkPurple),
                border = BorderStroke(1.5.dp, BrassGold),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = viewModel.constraintAlertTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = BrassGold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = viewModel.constraintAlertText,
                        fontSize = 13.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.showConstraintAlert = false },
                        colors = ButtonDefaults.buttonColors(containerColor = LatexCrimson),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("RELEASED 🔓", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
    
    // EXCLUSIVE REQUESTED: Turn-Start Cinematic Overlay (Whole board fully zoomed out, tapping zooms camera to player!)
    if (showTurnStartPrompt) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    showTurnStartPrompt = false
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "🕰️ THE COVENANT AWAKENS 🔓",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrassGold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "It's now ${activePlayer.name}'s turn!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = activePlayer.color,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Tap anywhere on screen to zoom in & roll...",
                    fontSize = 11.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
"""

content = content.replace(content[start_idx:end_idx], new_board_screen_code)

with open(file_path_board, "w", encoding="utf-8") as f:
    f.write(content)

print("SUCCESS! Completed board screen cinematic camera patches inside BoardScreen.kt!")
