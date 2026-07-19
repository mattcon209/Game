#!/usr/bin/env python3
"""CI patch script - applies visual fixes after compile_master_apk_final_v2.py generates source."""
import os
import re

print("=== Applying CI patches ===")

# 1. Fix PillarsOfSummoning.kt - pillar scale + dice
pillars_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/PillarsOfSummoning.kt"
if os.path.exists(pillars_path):
    with open(pillars_path, 'r') as f:
        c = f.read()
    # Scale fixes
    c = c.replace('val targetW = w * 3f', 'val targetW = w * 0.95f')
    c = c.replace('val targetH = h * 3f', 'val targetH = h * 1.7f')
    c = c.replace('tiltX * density * 0.4f', 'tiltX * density * 0.15f')
    c = c.replace('tiltY * density * 0.4f', 'tiltY * density * 0.15f')
    c = c.replace('targetW * 0.12f * scale', 'targetW * 0.28f * scale')
    c = c.replace('targetH * 0.12f * scale', 'targetH * 0.16f * scale')
    c = c.replace('targetH * 0.28f', 'targetH * 0.25f')
    c = c.replace('targetW * 0.08f * scale', 'targetW * 0.18f * scale')
    c = c.replace('targetW * 0.14f * pulseScale', 'targetW * 0.22f * pulseScale')
    # Replace image-based dice with unicode dice (drawables don't exist)
    c = re.sub(r'R\.drawable\.gothic_dice_\d', '0', c)
    # Replace the entire DiceFace3D body to use Text instead of Image
    old_dice_body = '''Box(
        modifier = Modifier
            .size(size.dp)
            .shadow(6.dp, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = "Dice Face $value",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }'''
    new_dice_body = '''Box(
        modifier = Modifier
            .size(size.dp)
            .shadow(6.dp, RoundedCornerShape(10.dp))
            .background(Color(0xFF2D1B3E), RoundedCornerShape(10.dp))
            .border(1.5.dp, Color(0xFFC5A059), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when(value) {
                1 -> "\\u2680"; 2 -> "\\u2681"; 3 -> "\\u2682"
                4 -> "\\u2683"; 5 -> "\\u2684"; 6 -> "\\u2685"; else -> "\\u2680"
            },
            fontSize = (size * 0.6).sp,
            color = Color.White
        )
    }'''
    if old_dice_body in c:
        c = c.replace(old_dice_body, new_dice_body)
        # Remove the resId val that references missing drawables
        c = re.sub(r'    val resId = when.*?else -> 0\n    }\n', '', c, flags=re.DOTALL)
    with open(pillars_path, 'w') as f:
        f.write(c)
    print("  ✓ PillarsOfSummoning.kt patched")

# 2. Fix BoardScreen.kt - camera zoom + pawn offset + clip
board_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/BoardScreen.kt"
if os.path.exists(board_path):
    with open(board_path, 'r') as f:
        c = f.read()
    c = c.replace('1.0f else 1.9f', '1.0f else 1.25f')
    c = c.replace('cellSize * -1.18f', 'cellSize * -0.75f')
    c = c.replace('cellSizePx * 1.18f', 'cellSizePx * 0.75f')
    old_border = '.border(1.5.dp, SeductiveViolet.copy(alpha = 0.3f), RoundedCornerShape(12.dp))'
    if old_border in c and '.clip(RoundedCornerShape(12.dp))' not in c:
        c = c.replace(old_border, old_border + '\n                            .clip(RoundedCornerShape(12.dp))', 1)
    with open(board_path, 'w') as f:
        f.write(c)
    print("  ✓ BoardScreen.kt patched")

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

# 7. Fix OutOfMemoryError - increase JVM metaspace for Kotlin compiler
gradle_props = "PolyLoveMarble/gradle.properties"
if os.path.exists(gradle_props):
    with open(gradle_props, 'r') as f:
        c = f.read()
    if 'MaxMetaspaceSize' not in c:
        c = c.rstrip() + '\norg.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=1g -XX:+HeapDumpOnOutOfMemoryError\nkotlin.daemon.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=1g\n'
        with open(gradle_props, 'w') as f:
            f.write(c)
        print("  ✓ gradle.properties patched (JVM memory increased)")

print("=== All CI patches applied ===")
