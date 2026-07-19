import os
import subprocess
import sys

print("--- ULTIMATE BUILD ENGINE started ---")

# Define paths
cui_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/CommonUI.kt"
gos_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/GrimoireOfSafety.kt"
setup_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/SetupScreen.kt"
sb_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/ScoreboardPanel.kt"
win_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/WinScreen.kt"
gamedata_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/game/GameData.kt"
vm_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/GameViewModel.kt"
board_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/BoardScreen.kt"
manifest_path = "PolyLoveMarble/app/src/main/AndroidManifest.xml"

# 1. Normalize line endings of all files first to prevent patch failures
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

# 2. Patch GameData.kt (Move package to top, insert classes and database methods)
with open(gamedata_path, "r", encoding="utf-8") as f:
    gd_content = f.read()

# Fix package statement
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

# Add customPunishments list and getPunishmentsForSpellbook function to PromptDatabase
if "val customPunishments" not in gd_content:
    gd_content = gd_content.replace(
        "object PromptDatabase {",
        "object PromptDatabase {\n    val customPunishments = mutableStateListOf<BookletPunishment>()\n\n    fun getPunishmentsForSpellbook(spellbook: String): List<String> {\n        val customList = customPunishments.filter { it.packName == spellbook }.map { it.text }\n        return punishments + customList\n    }"
    )

with open(gamedata_path, "w", encoding="utf-8") as f:
    f.write(gd_content)
print("GameData.kt completely patched!")

# 3. Patch GameViewModel.kt (Inject shakeX, shakeY, tileLandingCounts)
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

# 4. Write EmberParticle.kt
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
print("EmberParticle.kt generated on disk!")

# 5. Write ChainsOfDesireLogo.kt (SetupScreen.kt imports this)
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
            androidx.compose.foundation.Canvas(modifier = Modifier.size(48.dp)) {
                val w = size.width
                val h = size.height
                
                // Left Wing
                val wingPath = Path().apply {
                    moveTo(w * 0.5f, h * 0.4f)
                    cubicTo(w * 0.3f, h * 0.1f, w * 0.1f, h * 0.2f, 0f, h * 0.4f)
                    cubicTo(w * 0.1f, h * 0.55f, w * 0.25f, h * 0.6f, w * 0.45f, h * 0.55f)
                    
                    // Right Wing
                    moveTo(w * 0.5f, h * 0.4f)
                    cubicTo(w * 0.7f, h * 0.1f, w * 0.9f, h * 0.2f, w, h * 0.4f)
                    cubicTo(w * 0.9f, h * 0.55f, w * 0.75f, h * 0.6f, w * 0.55f, h * 0.55f)
                    close()
                }
                drawPath(wingPath, color = LatexCrimson.copy(alpha = 0.85f))
                
                // Lock base
                drawCircle(color = BrassGold, radius = w * 0.22f, center = center)
                drawCircle(color = Color.Black, radius = w * 0.18f, center = center)
                
                // Keyhole
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
    }
}
"""
logo_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/ChainsOfDesireLogo.kt"
with open(logo_path, "w", encoding="utf-8") as f:
    f.write(logo_content)
print("ChainsOfDesireLogo.kt generated on disk!")

# 6. Patch GrimoireOfSafety.kt FontWeight imports
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

# 7. Patch WinScreen.kt (use OccultPlayerAvatar instead of old meeple Canvas)
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

# 8. Patch SetupScreen.kt and ScoreboardPanel.kt calls to use three parameters
with open(setup_path, "r", encoding="utf-8") as f:
    setup_content = f.read()

setup_content = setup_content.replace(
    "OccultPlayerAvatar(\n                                        playerColor = player.color,\n                                        playerName = player.name,\n                                        playerIndex = index,",
    "OccultPlayerAvatar(\n                                        playerColor = player.color,\n                                        playerIndex = index,"
)

with open(setup_path, "w", encoding="utf-8") as f:
    f.write(setup_content)
print("SetupScreen avatar calls patched!")

with open(sb_path, "r", encoding="utf-8") as f:
    sb_content = f.read()

sb_content = sb_content.replace(
    "OccultPlayerAvatar(\n                            playerColor = p.color,\n                            playerName = p.name,\n                            playerIndex = if (pIndex != -1) pIndex else 0,",
    "OccultPlayerAvatar(\n                            playerColor = p.color,\n                            playerIndex = if (pIndex != -1) pIndex else 0,"
)

with open(sb_path, "w", encoding="utf-8") as f:
    f.write(sb_content)
print("ScoreboardPanel avatar calls patched!")

# 9. Normalize everything once more before compile
for root, dirs, files in os.walk("PolyLoveMarble/"):
    for file in files:
        if file.endswith(".kt"):
            p = os.path.join(root, file)
            with open(p, "rb") as f:
                bc = f.read()
            c = bc.decode("utf-8", errors="ignore").replace("\r\n", "\n")
            with open(p, "w", encoding="utf-8") as f:
                f.write(c)

# 10. Touch Manifest
with open(manifest_path, "r", encoding="utf-8") as f:
    manifest = f.read()
manifest = manifest.replace("</manifest>", "     </manifest>")
with open(manifest_path, "w", encoding="utf-8") as f:
    f.write(manifest)
print("Manifest touched!")

# 11. Run rebuild with JDK 21 and LIVE STDOUT stream forwarding!
print("--- RUNNING REBUILD WITH JDK 21 (LIVE STREAM) ---")
my_env = os.environ.copy()
my_env["JAVA_HOME"] = "/usr/lib/jvm/java-21-openjdk-amd64"
my_env["PATH"] = "/usr/lib/jvm/java-21-openjdk-amd64/bin:" + my_env["PATH"]
my_env["ANDROID_HOME"] = "/home/user/android-sdk"

# We run subprocess.run without capturing output so it streams live to console!
result = subprocess.run(
    ["/home/user/gradle-bin/gradle-8.5/bin/gradle", "assembleDebug", "--no-daemon"],
    cwd="PolyLoveMarble/",
    env=my_env
)

if result.returncode == 0:
    print("SUCCESSFUL COMPILE! Copying APK...")
    subprocess.run(["cp", "PolyLoveMarble/app/build/outputs/apk/debug/app-debug.apk", "/home/user/PolyKinkMarble.apk"])
    print("Perfect APK compiled successfully!")
else:
    print(f"FAILED DIRECT BUILD! Code: {result.returncode}")
    sys.exit(1)
