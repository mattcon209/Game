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
        // Gold-leaf shackle-shield locked with keyhole, wrapped in velvet crimson gothic bat wings
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
            // Drawing the beautiful gothic bat wings and lock
            androidx.compose.foundation.Canvas(modifier = Modifier.size(48.dp)) {
                val w = size.width
                val h = size.height
                
                // 1. Draw crimson bat wings
                val wingPath = Path().apply {
                    // Left wing
                    moveTo(w * 0.5f, h * 0.4f)
                    cubicTo(w * 0.3f, h * 0.1f, w * 0.1f, h * 0.2f, 0f, h * 0.4f)
                    cubicTo(w * 0.1f, h * 0.55f, w * 0.25f, h * 0.6f, w * 0.45f, h * 0.55f)
                    
                    // Right wing
                    moveTo(w * 0.5f, h * 0.4f)
                    cubicTo(w * 0.7f, h * 0.1f, w * 0.9f, h * 0.2f, w, h * 0.4f)
                    cubicTo(w * 0.9f, h * 0.55f, w * 0.75f, h * 0.6f, w * 0.55f, h * 0.55f)
                    close()
                }
                drawPath(wingPath, color = LatexCrimson.copy(alpha = 0.85f))
                
                // 2. Draw gold shackle-shield center
                drawCircle(
                    color = BrassGold,
                    radius = w * 0.22f,
                    center = center
                )
                drawCircle(
                    color = Color.Black,
                    radius = w * 0.18f,
                    center = center
                )
                
                // 3. Draw keyhole inside lock
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
        
        // Gothic Title Header
        Text(
            text = "CHAINS OF DESIRE",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BrassGold,
            fontFamily = FontFamily.Serif,
            letterSpacing = 1.5.sp,
            textAlign = TextAlign.Center
        )
        
        // Seductive subtitle
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
    # Clean duplicates if any
    cui_content = cui_content.replace(
        "import androidx.compose.ui.graphics.drawscope.DrawScope\nimport androidx.compose.ui.graphics.drawscope.DrawScope",
        "import androidx.compose.ui.graphics.drawscope.DrawScope"
    )

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

# 7. Patch WinScreen.kt
with open(win_path, "r", encoding="utf-8") as f:
    win_content = f.read()
old_win_import = "import androidx.compose.foundation.background"
new_win_import = "import androidx.compose.foundation.background\nimport androidx.compose.foundation.border"
if "import androidx.compose.foundation.border" not in win_content:
    win_content = win_content.replace(old_win_import, new_win_import)
    print("WinScreen imports patched!")
with open(win_path, "w", encoding="utf-8") as f:
    f.write(win_content)

# 8. Patch BoardScreen.kt
with open(board_path, "r", encoding="utf-8") as f:
    board_content = f.read()

# BackHandler Import
old_board_import = "package com.polylove.marble.ui.screens"
new_board_import = "package com.polylove.marble.ui.screens\n\nimport androidx.activity.compose.BackHandler\n"
if "import androidx.activity.compose.BackHandler" not in board_content:
    board_content = board_content.replace(old_board_import, new_board_import)
    print("Added BackHandler import!")

# BackHandler Call
old_board_density = "val density = LocalDensity.current.density"
new_board_density = "val density = LocalDensity.current.density\n    \n    // Intercept system Back button to exit session!\n    BackHandler(enabled = true) {\n        viewModel.currentScreen = GameScreen.Setup\n    }"
if "Intercept system Back button to exit session" not in board_content:
    board_content = board_content.replace(old_board_density, new_board_density)
    print("Added BackHandler call!")

# Leaping math
old_leap_block = """                                            // Compute jumping arc offsets
                                            val progress = viewModel.animatedPlayerStepProgress
                                            val arcY = -kotlin.math.sin(progress * Math.PI).toFloat() * 32f * density
                                            
                                            // Find source offset to slide from
                                            val (srcCol, srcRow) = BoardCreator.getGridCoordinates(viewModel.animatedPlayerSourceTile, viewModel.gridSize)
                                            val srcLeft = cellSize * srcCol
                                            val srcTop = cellSize * srcRow
                                            
                                            val currentLeapX = srcLeft + (leftOffset - srcLeft) * progress
                                            val currentLeapY = srcTop + (topOffset - srcTop) * progress + arcY - (14f * density)"""

new_leap_block = """                                            // Compute jumping arc offsets
                                             val progress = viewModel.animatedPlayerStepProgress
                                             val (srcCol, srcRow) = BoardCreator.getGridCoordinates(viewModel.animatedPlayerSourceTile, viewModel.gridSize)
                                             val srcTop = cellSize * srcRow
                                             val currentLeapY = srcTop + (topOffset - srcTop) * progress + (-kotlin.math.sin(progress * Math.PI).toFloat() * 32f).dp - 14.dp"""

if old_leap_block in board_content:
    board_content = board_content.replace(old_leap_block, new_leap_block)
    print("BoardScreen leaping math patched!")
else:
    # Fuzzy replace leaping math
    idx = board_content.find("// Compute jumping arc offsets")
    if idx != -1:
        end_idx = board_content.find("// Squish and stretch meeple", idx)
        if end_idx != -1:
            board_content = board_content[:idx] + """// Compute jumping arc offsets
                                             val progress = viewModel.animatedPlayerStepProgress
                                             val (srcCol, srcRow) = BoardCreator.getGridCoordinates(viewModel.animatedPlayerSourceTile, viewModel.gridSize)
                                             val srcTop = cellSize * srcRow
                                             val currentLeapY = srcTop + (topOffset - srcTop) * progress + (-kotlin.math.sin(progress * Math.PI).toFloat() * 32f).dp - 14.dp
                                             
                                             """ + board_content[end_idx:]
            print("Fuzzy patched BoardScreen leaping math!")

# Ensure imageResource is imported in BoardScreen
if "import androidx.compose.ui.res.imageResource" not in board_content:
    board_content = board_content.replace(
        "import androidx.compose.ui.window.Dialog",
        "import androidx.compose.ui.window.Dialog\nimport androidx.compose.ui.res.imageResource"
    )

with open(board_path, "w", encoding="utf-8") as f:
    f.write(board_content)

# 9. Patch SetupScreen.kt (remove playerName parameter from OccultPlayerAvatar)
with open(setup_path, "r", encoding="utf-8") as f:
    setup_content = f.read()

setup_content = setup_content.replace(
    "OccultPlayerAvatar(\n                                        playerColor = player.color,\n                                        playerName = player.name,\n                                        playerIndex = index,",
    "OccultPlayerAvatar(\n                                        playerColor = player.color,\n                                        playerIndex = index,"
)
with open(setup_path, "w", encoding="utf-8") as f:
    f.write(setup_content)
print("SetupScreen.kt avatar call patched!")

# 10. Patch ScoreboardPanel.kt (remove playerName parameter from OccultPlayerAvatar)
with open(sb_path, "r", encoding="utf-8") as f:
    sb_content = f.read()

sb_content = sb_content.replace(
    "OccultPlayerAvatar(\n                            playerColor = p.color,\n                            playerName = p.name,\n                            playerIndex = if (pIndex != -1) pIndex else 0,",
    "OccultPlayerAvatar(\n                            playerColor = p.color,\n                            playerIndex = if (pIndex != -1) pIndex else 0,"
)
with open(sb_path, "w", encoding="utf-8") as f:
    f.write(sb_content)
print("ScoreboardPanel.kt avatar call patched!")

# 11. Normalize all line endings once more to prevent compile failures
for root, dirs, files in os.walk("PolyLoveMarble/"):
    for file in files:
        if file.endswith(".kt"):
            p = os.path.join(root, file)
            with open(p, "rb") as f:
                bc = f.read()
            c = bc.decode("utf-8", errors="ignore").replace("\r\n", "\n")
            with open(p, "w", encoding="utf-8") as f:
                f.write(c)

print("--- RUNNING GRADLE BUILD ---")
# Run assembly debug directly
result = subprocess.run(
    ["/home/user/gradle-bin/gradle-8.5/bin/gradle", "assembleDebug", "--no-daemon"],
    cwd="PolyLoveMarble/",
    capture_output=True,
    text=True
)

print("Gradle STDOUT:")
print(result.stdout[-1500:])
print("Gradle STDERR:")
print(result.stderr)

if result.returncode == 0:
    print("SUCCESSFUL BUILD! Copying APK...")
    subprocess.run(["cp", "PolyLoveMarble/app/build/outputs/apk/debug/app-debug.apk", "/home/user/PolyKinkMarble.apk"])
    print("Master APK built successfully!")
else:
    print(f"FAILED MASTER COMPILE! Code: {result.returncode}")
    exit(1)
