#!/usr/bin/env python3
"""CI patch script - applies visual fixes after compile_master_apk_final_v2.py generates source."""
import os
import re

print("=== Applying CI patches ===")

# 0. Fix GameData.kt import order (import before package = fatal)
gamedata_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/game/GameData.kt"
if os.path.exists(gamedata_path):
    with open(gamedata_path, 'r') as f:
        c = f.read()
    # Fix import before package
    if c.startswith('import ') and '\npackage ' in c[:100]:
        lines = c.split('\n')
        pkg_idx = next(i for i, l in enumerate(lines) if l.startswith('package '))
        imports_before_pkg = lines[:pkg_idx]
        rest = lines[pkg_idx:]
        # Rebuild: package first, then all imports
        c = '\n'.join(rest[:1] + [''] + imports_before_pkg + rest[1:])
    # Add missing classes if not present
    if 'data class ActiveConstraint' not in c:
        insert_after = 'data class Prompt(\n    val category: TileType,\n    val spiceLevel: SpiceLevel,\n    val template: String,\n    val packName: String = "Base Deck"\n)'
        new_classes = """data class Prompt(
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
        if insert_after in c:
            c = c.replace(insert_after, new_classes)
    # Add getPunishmentsForSpellbook if missing
    if 'getPunishmentsForSpellbook' not in c and 'object PromptDatabase' in c:
        c = c.replace('object PromptDatabase {', 'object PromptDatabase {\n    val customPunishments = mutableStateListOf<BookletPunishment>()\n    fun getPunishmentsForSpellbook(spellbook: String): List<String> = punishments')
    with open(gamedata_path, 'w') as f:
        f.write(c)
    print("  ✓ GameData.kt patched (import order + missing classes)")

# 0b. Fix GameViewModel.kt - add missing properties
vm_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/GameViewModel.kt"
if os.path.exists(vm_path):
    with open(vm_path, 'r') as f:
        c = f.read()
    additions = ""
    if 'var shakeX' not in c:
        additions += "    var shakeX by mutableStateOf(0f)\n    var shakeY by mutableStateOf(0f)\n"
    if 'tileLandingCounts' not in c:
        additions += "    val tileLandingCounts = mutableMapOf<Int, Int>()\n"
    if 'customPunishments' not in c:
        additions += "    val customPunishments = mutableStateListOf<BookletPunishment>()\n"
    if additions:
        # Insert after "var victorySummaryText"
        c = c.replace('var victorySummaryText by mutableStateOf("")', 'var victorySummaryText by mutableStateOf("")\n' + additions)
        with open(vm_path, 'w') as f:
            f.write(c)
        print("  ✓ GameViewModel.kt patched (missing properties)")

# 1. Fix PillarsOfSummoning.kt - pillar scale + dice
pillars_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/PillarsOfSummoning.kt"
if os.path.exists(pillars_path):
    with open(pillars_path, 'r') as f:
        c = f.read()
    # Scale fixes
    c = c.replace('val targetW = w * 3f', 'val targetW = w * 0.95f')
    c = c.replace('val targetH = h * 3f', 'val targetH = h * 1.4f')
    c = c.replace('tiltX * density * 0.4f', 'tiltX * density * 0.15f')
    c = c.replace('tiltY * density * 0.4f', 'tiltY * density * 0.15f')
    c = c.replace('targetW * 0.12f * scale', 'targetW * 0.28f * scale')
    c = c.replace('targetH * 0.12f * scale', 'targetH * 0.16f * scale')
    c = c.replace('targetH * 0.28f', 'targetH * 0.25f')
    c = c.replace('targetW * 0.08f * scale', 'targetW * 0.18f * scale')
    c = c.replace('targetW * 0.14f * pulseScale', 'targetW * 0.22f * pulseScale')
    # Replace the ENTIRE DiceFace3D function with a safe text-based version
    # Use regex to match the whole function regardless of whitespace
    dice_pattern = r'@Composable\nfun DiceFace3D\(value: Int, size: Int = \d+\) \{.*?^\}'
    dice_replacement = '''@Composable
fun DiceFace3D(value: Int, size: Int = 44) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .shadow(6.dp, RoundedCornerShape(10.dp))
            .background(Color(0xFF2D1B3E), RoundedCornerShape(10.dp))
            .border(1.5.dp, Color(0xFFC5A059), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when(value) {
                1 -> "\u2680"; 2 -> "\u2681"; 3 -> "\u2682"
                4 -> "\u2683"; 5 -> "\u2684"; 6 -> "\u2685"; else -> "\u2680"
            },
            fontSize = (size * 0.6).sp,
            color = Color.White
        )
    }
}'''
    c = re.sub(dice_pattern, dice_replacement, c, flags=re.DOTALL | re.MULTILINE)
    # Also ensure needed imports are present
    if 'import androidx.compose.foundation.background' not in c:
        c = c.replace('import androidx.compose.foundation.Canvas', 'import androidx.compose.foundation.Canvas\nimport androidx.compose.foundation.background')
    if 'import androidx.compose.ui.text.font.FontWeight' not in c and 'FontWeight' not in c:
        pass  # Text doesn't need FontWeight here
    # Add missing imports for the new DiceFace3D
    if 'import androidx.compose.foundation.border' not in c:
        c = c.replace('import androidx.compose.foundation.Canvas', 'import androidx.compose.foundation.Canvas\nimport androidx.compose.foundation.border')
    if 'import androidx.compose.ui.unit.sp' not in c:
        c = c.replace('import androidx.compose.ui.unit.dp', 'import androidx.compose.ui.unit.dp\nimport androidx.compose.ui.unit.sp')
    with open(pillars_path, 'w') as f:
        f.write(c)
    print("  ✓ PillarsOfSummoning.kt patched")

# 2. Fix BoardScreen.kt - camera zoom + pawn offset + clip
board_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/BoardScreen.kt"
if os.path.exists(board_path):
    with open(board_path, 'r') as f:
        c = f.read()
    c = c.replace('1.0f else 1.9f', '1.0f else 1.0f')  # Disable zoom temporarily to fix crash
    c = c.replace('cellSize * -1.18f', 'cellSize * -0.55f')
    c = c.replace('cellSizePx * 1.18f', 'cellSizePx * 0.55f')
    # NOTE: Do NOT add .clip() here - it clips the 3D pillar tops!
    # Remove clip if the compile script added one
    c = c.replace('.clip(RoundedCornerShape(12.dp))\n', '')
    c = c.replace('\n                            .clip(RoundedCornerShape(12.dp))', '')
    # Add crash protection: wrap sensor registration in try-catch
    c = c.replace(
        'val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager',
        'val sensorManager = try { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager } catch (e: Exception) { null }'
    )
    c = c.replace(
        'sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)',
        'if (sensorManager != null && accelerometer != null) sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)'
    )
    c = c.replace(
        'sensorManager.unregisterListener(listener)',
        'sensorManager?.unregisterListener(listener)'
    )
    # Also protect the DisposableEffect onDispose
    c = c.replace(
        'val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)',
        'val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)'
    )
    with open(board_path, 'w') as f:
        f.write(c)
    print("  ✓ BoardScreen.kt patched (zoom disabled, crash protection added)")

# 3. Fix CommonUI.kt - add missing imports
cui_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/CommonUI.kt"
if os.path.exists(cui_path):
    with open(cui_path, 'r') as f:
        c = f.read()
    if 'import androidx.compose.foundation.Canvas' not in c:
        c = c.replace('import androidx.compose.foundation.background',
                      'import androidx.compose.foundation.Canvas\nimport androidx.compose.foundation.background')
    if 'import androidx.compose.ui.geometry.CornerRadius' not in c:
        c = c.replace('import androidx.compose.ui.geometry.Offset',
                      'import androidx.compose.ui.geometry.CornerRadius\nimport androidx.compose.ui.geometry.Offset')
    with open(cui_path, 'w') as f:
        f.write(c)
    print("  ✓ CommonUI.kt patched")

# 4. Fix GrimoireOfSafety.kt - Book icon
gos_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/GrimoireOfSafety.kt"
if os.path.exists(gos_path):
    with open(gos_path, 'r') as f:
        c = f.read()
    c = c.replace('Icons.Filled.Book', 'Icons.Filled.MenuBook')
    with open(gos_path, 'w') as f:
        f.write(c)
    print("  ✓ GrimoireOfSafety.kt patched")

# 5. Fix SetupScreen.kt - shadow import + Remove icon
setup_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/SetupScreen.kt"
if os.path.exists(setup_path):
    with open(setup_path, 'r') as f:
        c = f.read()
    if 'import androidx.compose.ui.draw.shadow' not in c:
        c = c.replace('import androidx.compose.ui.draw.clip',
                      'import androidx.compose.ui.draw.clip\nimport androidx.compose.ui.draw.shadow')
    c = c.replace('Icons.Filled.Remove', 'Icons.Filled.Delete')
    with open(setup_path, 'w') as f:
        f.write(c)
    print("  ✓ SetupScreen.kt patched")

# 6. Add material-icons-extended to build.gradle.kts
gradle_path = "PolyLoveMarble/app/build.gradle.kts"
if os.path.exists(gradle_path):
    with open(gradle_path, 'r') as f:
        c = f.read()
    if 'material-icons-extended' not in c and 'material.icons.extended' not in c:
        c = c.replace(
            'implementation(libs.androidx.compose.material3)',
            'implementation(libs.androidx.compose.material3)\n    implementation(libs.androidx.compose.material.icons.extended)'
        )
        with open(gradle_path, 'w') as f:
            f.write(c)
        print("  ✓ build.gradle.kts patched (icons-extended)")

print("=== All CI patches applied ===")

# 7. Fix OutOfMemoryError - REPLACE restrictive Termux memory limits with CI-appropriate values
gradle_props = "PolyLoveMarble/gradle.properties"
if os.path.exists(gradle_props):
    with open(gradle_props, 'r') as f:
        c = f.read()
    # Replace the low-memory jvmargs line
    import re
    c = re.sub(r'org\.gradle\.jvmargs=.*', 'org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=1g -XX:+UseParallelGC -Dfile.encoding=UTF-8', c)
    # Remove in-process strategy (let Kotlin compiler use its own process with more memory)
    c = c.replace('kotlin.compiler.execution.strategy=in-process', '# kotlin.compiler.execution.strategy=in-process  # disabled for CI')
    # Allow parallel workers on CI
    c = c.replace('org.gradle.workers.max=1', 'org.gradle.workers.max=4')
    c = c.replace('org.gradle.parallel=false', 'org.gradle.parallel=true')
    with open(gradle_props, 'w') as f:
        f.write(c)
    print("  ✓ gradle.properties patched (4GB heap, 1GB metaspace, parallel workers)")

print("=== All CI patches applied ===")
