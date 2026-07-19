import os
import subprocess

print("--- master compile final v3 started ---")

# Define paths at absolute top
cui_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/CommonUI.kt"
gos_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/GrimoireOfSafety.kt"
setup_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/SetupScreen.kt"
sb_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/ScoreboardPanel.kt"
win_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/WinScreen.kt"
gamedata_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/game/GameData.kt"
vm_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/GameViewModel.kt"
board_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/BoardScreen.kt"
pawn_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/GamePawn.kt"
pillars_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/PillarsOfSummoning.kt"
manifest_path = "PolyLoveMarble/app/src/main/AndroidManifest.xml"

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

# 1. Patch GameData.kt Package declaration & Data Classes & Methods
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

# 2. Patch GameViewModel.kt
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

# 3. Patch GrimoireOfSafety.kt
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

# 4. Patch WinScreen.kt (Replace drawGamePawn with OccultPlayerAvatar)
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

# 5. Patch SetupScreen.kt (Add shadow import & fix avatar call parameters)
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

# 6. Patch ScoreboardPanel.kt
with open(sb_path, "r", encoding="utf-8") as f:
    sb_content = f.read()
sb_content = sb_content.replace(
    "OccultPlayerAvatar(\n                            playerColor = p.color,\n                            playerName = p.name,\n                            playerIndex = if (pIndex != -1) pIndex else 0,",
    "OccultPlayerAvatar(\n                            playerColor = p.color,\n                            playerIndex = if (pIndex != -1) pIndex else 0,"
)
with open(sb_path, "w", encoding="utf-8") as f:
    f.write(sb_content)
print("ScoreboardPanel.kt patched!")

# 7. Normalize all line endings once more to prevent compile failures
for root, dirs, files in os.walk("PolyLoveMarble/"):
    for file in files:
        if file.endswith(".kt"):
            p = os.path.join(root, file)
            with open(p, "rb") as f:
                bc = f.read()
            c = bc.decode("utf-8", errors="ignore").replace("\r\n", "\n")
            with open(p, "w", encoding="utf-8") as f:
                f.write(c)

# 8. Touch AndroidManifest.xml
with open(manifest_path, "r", encoding="utf-8") as f:
    manifest = f.read()
manifest = manifest.replace("</manifest>", "    </manifest>")
with open(manifest_path, "w", encoding="utf-8") as f:
    f.write(manifest)
print("AndroidManifest touched!")

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
