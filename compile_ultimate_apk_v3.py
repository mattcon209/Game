import os
import subprocess

print("--- master compile final v2 started ---")

# Define paths at absolute top
cui_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/CommonUI.kt"
gos_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/GrimoireOfSafety.kt"
setup_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/SetupScreen.kt"
sb_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/ScoreboardPanel.kt"
win_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/WinScreen.kt"
gamedata_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/game/GameData.kt"
vm_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/GameViewModel.kt"
board_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/BoardScreen.kt"

# Normalize all line endings first
for root, dirs, files in os.walk("PolyLoveMarble/"):
    for file in files:
        if file.endswith(".kt"):
            p = os.path.join(root, file)
            with open(p, "rb") as f:
                bc = f.read()
            c = bc.decode("utf-8", errors="ignore").replace("\r\n", "\n")
            with open(p, "w", encoding="utf-8") as f:
                f.write(c)

print("Line endings normalized!")

# 1. Write EmberParticle.kt
ep_content = """package com.polylove.marble.ui

data class EmberParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var size: Float,
    var alpha: Float,
    var life: Float,
    var maxLife: Float
)
"""
ep_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/EmberParticle.kt"
os.makedirs(os.path.dirname(ep_path), exist_ok=True)
with open(ep_path, "w", encoding="utf-8") as f:
    f.write(ep_content)
print("Wrote EmberParticle.kt!")

# 2. Write ChainsOfDesireLogo.kt
logo_content = """package com.polylove.marble.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.ui.theme.BrassGold
import com.polylove.marble.ui.theme.LatexCrimson
import com.polylove.marble.ui.theme.LeatherDarkPurple
import com.polylove.marble.ui.theme.SeductiveViolet

@Composable
fun ChainsOfDesireLogo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(SeductiveViolet, Color.Black),
                        radius = 120f
                    )
                )
                .border(2.dp, BrassGold, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.size(48.dp)) {
                val w = size.width
                val h = size.height
                val wingPath = Path().apply {
                    moveTo(w * 0.5f, h * 0.4f)
                    cubicTo(w * 0.3f, h * 0.1f, w * 0.1f, h * 0.2f, 0f, h * 0.4f)
                    cubicTo(w * 0.1f, h * 0.55f, w * 0.25f, h * 0.6f, w * 0.45f, h * 0.55f)
                    moveTo(w * 0.5f, h * 0.4f)
                    cubicTo(w * 0.7f, h * 0.1f, w * 0.9f, h * 0.2f, w, h * 0.4f)
                    cubicTo(w * 0.9f, h * 0.55f, w * 0.75f, h * 0.6f, w * 0.55f, h * 0.55f)
                    close()
                }
                drawPath(wingPath, color = LatexCrimson.copy(alpha = 0.85f))
                drawCircle(color = BrassGold, radius = w * 0.22f, center = center)
                drawCircle(color = Color.Black, radius = w * 0.18f, center = center)
                val keyholePath = Path().apply {
                    addOval(Rect(center = center, radius = w * 0.06f))
                    moveTo(w * 0.47f, h * 0.52f)
                    lineTo(w * 0.53f, h * 0.52f)
                    lineTo(w * 0.55f, h * 0.68f)
                    lineTo(w * 0.45f, h * 0.68f)
                    close()
                }
                drawPath(keyholePath, color = BrassGold)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "CHAINS OF DESIRE",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BrassGold,
            fontFamily = FontFamily.Serif,
            letterSpacing = 1.5.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "An Intimate Poly-BDSM Summoning Ritual",
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = LatexCrimson,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
"""
logo_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/ChainsOfDesireLogo.kt"
with open(logo_path, "w", encoding="utf-8") as f:
    f.write(logo_content)
print("Wrote ChainsOfDesireLogo.kt!")

# 3. Patch GameData.kt Package declaration & Data Classes & Methods
with open(gamedata_path, "r", encoding="utf-8") as f:
    gd_content = f.read()

# Put package statement on top
gd_content = gd_content.replace(
    "import androidx.compose.runtime.mutableStateListOf\npackage com.polylove.marble.game",
    "package com.polylove.marble.game\n\nimport androidx.compose.runtime.mutableStateListOf"
)

# Insert Prompt duration variables, ActiveConstraint, and BookletPunishment
old_prompt = """data class Prompt(
    val category: TileType,
    val spiceLevel: SpiceLevel,
    val template: String,
    val packName: String = "Base Deck"
)"""
new_prompt = """data class Prompt(
    val category: TileType,
    val spiceLevel: SpiceLevel,
    val template: String,
    val packName: String = "Base Deck",
    val durationValue: Int = 0,
    val durationUnit: String = ""
)

data class ActiveConstraint(
    val id: String = "",
    val playerName: String,
    val description: String,
    val durationValue: Int,
    val durationUnit: String,
    var remainingSeconds: Int,
    var remainingTurns: Int = 0,
    var remainingTimes: Int
)

data class BookletPunishment(
    val text: String,
    val packName: String
)"""
if old_prompt in gd_content:
    gd_content = gd_content.replace(old_prompt, new_prompt)

# Add customPunishments and getPunishmentsForSpellbook to PromptDatabase
if "val customPunishments" not in gd_content:
    gd_content = gd_content.replace(
        "object PromptDatabase {",
        "object PromptDatabase {\n    val customPunishments = mutableStateListOf<BookletPunishment>()\n\n    fun getPunishmentsForSpellbook(spellbook: String): List<String> {\n        val customList = customPunishments.filter { it.packName == spellbook }.map { it.text }\n        return punishments + customList\n    }"
    )

with open(gamedata_path, "w", encoding="utf-8") as f:
    f.write(gd_content)
print("GameData.kt fully patched!")

# 4. Patch GameViewModel.kt
with open(vm_path, "r", encoding="utf-8") as f:
    vm_content = f.read()

target = "animatedPlayerStepProgress"
idx = vm_content.find(target)
if idx != -1 and "shakeX" not in vm_content:
    end_idx = vm_content.find("\n", idx)
    insertion = "\n    var shakeX by mutableStateOf(0f)\n    var shakeY by mutableStateOf(0f)\n    val tileLandingCounts = mutableStateMapOf<Int, Int>()"
    vm_content = vm_content[:end_idx] + insertion + vm_content[end_idx:]
    print("Patched GameViewModel with shakeX, shakeY, and tileLandingCounts!")

with open(vm_path, "w", encoding="utf-8") as f:
    f.write(vm_content)

# 5. Patch CommonUI.kt
with open(cui_path, "r", encoding="utf-8") as f:
    cui_content = f.read()

old_cui_imports = "import androidx.compose.ui.graphics.drawscope.Stroke"
new_cui_imports = """import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath"""

if "import androidx.compose.ui.graphics.drawscope.DrawScope" not in cui_content:
    cui_content = cui_content.replace(old_cui_imports, new_cui_imports)
    print("CommonUI imports patched!")
else:
    # Ensure there is no duplicate
    cui_content = cui_content.replace(
        "import androidx.compose.ui.graphics.drawscope.DrawScope\nimport androidx.compose.ui.graphics.drawscope.DrawScope",
        "import androidx.compose.ui.graphics.drawscope.DrawScope"
    )
    print("Cleaned duplicate imports inside CommonUI.kt!")

with open(cui_path, "w", encoding="utf-8") as f:
    f.write(cui_content)

# 6. Patch GrimoireOfSafety.kt
if os.path.exists(gos_path):
    with open(gos_path, "r", encoding="utf-8") as f:
        gos_content = f.read()
    old_gos_import = "import androidx.compose.ui.Modifier"
    new_gos_import = "import androidx.compose.ui.Modifier\nimport androidx.compose.ui.text.font.FontWeight"
    if "import androidx.compose.ui.text.font.FontWeight" not in gos_content:
        gos_content = gos_content.replace(old_gos_import, new_gos_import)
        print("GrimoireOfSafety imports patched!")
    with open(gos_path, "w", encoding="utf-8") as f:
        f.write(gos_content)

# 7. Patch WinScreen.kt (Replace drawGamePawn with OccultPlayerAvatar)
with open(win_path, "r", encoding="utf-8") as f:
    win_content = f.read()

old_win_pawn = """                                    Box(modifier = Modifier.size(16.dp)) {
                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                            drawGamePawn(color = p.color, x = size.width/2f, y = size.height/2f, size = 12f * density)
                                        }
                                    }"""

new_win_pawn = """                                    val pIndex = viewModel.players.indexOf(p)
                                    OccultPlayerAvatar(
                                        playerColor = p.color,
                                        playerIndex = if (pIndex != -1) pIndex else 0,
                                        modifier = Modifier.size(16.dp)
                                    )"""

if old_win_pawn in win_content:
    win_content = win_content.replace(old_win_pawn, new_win_pawn)
    print("WinScreen.kt patched!")

if "import androidx.compose.foundation.border" not in win_content:
    win_content = win_content.replace(
        "import androidx.compose.foundation.background",
        "import androidx.compose.foundation.background\nimport androidx.compose.foundation.border"
    )

with open(win_path, "w", encoding="utf-8") as f:
    f.write(win_content)

# 8. Patch SetupScreen.kt (Add shadow import & fix avatar call parameters)
with open(setup_path, "r", encoding="utf-8") as f:
    setup_content = f.read()

if "import androidx.compose.ui.draw.shadow" not in setup_content:
    setup_content = setup_content.replace(
        "import androidx.compose.ui.draw.clip",
        "import androidx.compose.ui.draw.clip\nimport androidx.compose.ui.draw.shadow"
    )

setup_content = setup_content.replace(
    "OccultPlayerAvatar(\n                                        playerColor = player.color,\n                                        playerName = player.name,\n                                        playerIndex = index,",
    "OccultPlayerAvatar(\n                                        playerColor = player.color,\n                                        playerIndex = index,"
)

with open(setup_path, "w", encoding="utf-8") as f:
    f.write(setup_content)
print("SetupScreen.kt patched!")

# 9. Patch ScoreboardPanel.kt
with open(sb_path, "r", encoding="utf-8") as f:
    sb_content = f.read()
sb_content = sb_content.replace(
    "OccultPlayerAvatar(\n                            playerColor = p.color,\n                            playerName = p.name,\n                            playerIndex = if (pIndex != -1) pIndex else 0,",
    "OccultPlayerAvatar(\n                            playerColor = p.color,\n                            playerIndex = if (pIndex != -1) pIndex else 0,"
)
with open(sb_path, "w", encoding="utf-8") as f:
    f.write(sb_content)
print("ScoreboardPanel.kt patched!")

# 10. Write BoardScreen.kt cleanly and completely
board_content = """package com.polylove.marble.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.polylove.marble.game.*
import com.polylove.marble.R
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
    
    // Intercept system Back button to exit session!
    BackHandler(enabled = true) {
        viewModel.currentScreen = GameScreen.Setup
    }
    
    // Load the actual photorealistic 3D hooded pawns image!
    val pawnsBitmap = ImageBitmap.imageResource(id = R.drawable.gothic_pawns)
    
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
    val targetZoom = if (showTurnStartPrompt) 1.0f else 1.9f

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
                    tiltX = event.values[0] // Yaw tilt
                    tiltY = event.values[1] // Pitch tilt
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.currentScreen = GameScreen.Setup
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.ArrowBack, "Exit Session", tint = LatexCrimson)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "It's ${activePlayer.name}'s turn!",
                                    fontSize = 15.sp,
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
                        }
                        // Animated 3D Mini-Pawn Preview with bat wings!
                        Box(modifier = Modifier.size(24.dp)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawGamePawn(pawnsBitmap = pawnsBitmap, playerColor = activePlayer.color, x = size.width/2, y = size.height/2, size = 18f * density)
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
                                    
                                    // 3D glossy game pawns/meeples stand centered and OFFSET UPWARDS TO PREVENT SQUISHING!
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
                                                modifier = Modifier.wrapContentSize()
                                            ) {
                                                playersOnThisTile.forEach { playerOnTile ->
                                                    Box(
                                                        modifier = Modifier
                                                            .size(if (viewModel.gridSize == 6) 18.dp else 22.dp)
                                                            .padding(horizontal = 0.5.dp)
                                                    ) {
                                                        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                                            drawGamePawn(
                                                                pawnsBitmap = pawnsBitmap,
                                                                playerColor = playerOnTile.color,
                                                                x = size.width / 2,
                                                                y = size.height / 2,
                                                                size = (if (viewModel.gridSize == 6) 15f else 18f) * density
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
                                            val (srcCol, srcRow) = BoardCreator.getGridCoordinates(viewModel.animatedPlayerSourceTile, viewModel.gridSize)
                                             val srcTop = cellSize * srcRow
                                             val currentLeapY = srcTop + (topOffset - srcTop) * progress + (-kotlin.math.sin(progress * Math.PI).toFloat() * 32f).dp - 14.dp
                                            
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
                                                    modifier = Modifier.size(if (viewModel.gridSize == 6) 18.dp else 22.dp)
                                                ) {
                                                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                                        drawGamePawn(
                                                            pawnsBitmap = pawnsBitmap,
                                                            playerColor = activeLeapingPlayer.color,
                                                            x = size.width / 2,
                                                            y = size.height / 2,
                                                            size = (if (viewModel.gridSize == 6) 15f else 18f) * density,
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
                        colors = CardDefaults.cardColors(containerColor = LeatherDarkPurple.copy(alpha = 0.9f)),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, SeductiveViolet.copy(alpha = 0.4f)),
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
                                        color = SilkPurple,
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
                colors = CardDefaults.cardColors(containerColor = LeatherDarkPurple),
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
                        color = BrassGold,
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
                colors = CardDefaults.cardColors(containerColor = LeatherDarkPurple),
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
                        color = BrassGold,
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
"""

with open(board_path, "w", encoding="utf-8") as f:
    f.write(board_content)
print("Wrote BoardScreen.kt cleanly and completely!")

# 11. Recompile and package
print("--- RUNNING REBUILD ---")
result = subprocess.run(
    ["/home/user/gradle-bin/gradle-8.5/bin/gradle", "assembleDebug", "--no-daemon", "--rerun-tasks"],
    cwd="PolyLoveMarble/",
    capture_output=True,
    text=True
)

print("Gradle STDOUT:")
print(result.stdout[-1500:])
print("Gradle STDERR:")
print(result.stderr)

if result.returncode == 0:
    print("SUCCESSFUL COMPILE! Copying APK...")
    subprocess.run(["cp", "PolyLoveMarble/app/build/outputs/apk/debug/app-debug.apk", "/home/user/PolyKinkMarble.apk"])
    print("Master APK compiled successfully!")
else:
    print(f"FAILED PERFECT BUILD! Code: {result.returncode}")
    exit(1)
