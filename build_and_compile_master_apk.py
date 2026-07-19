import os
import subprocess

print("--- MASTER COMPILE REBUILD PROCESS started ---")

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

# 1. Write GamePawn.kt
pawn_content = """package com.polylove.marble.ui.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

// EXCLUSIVE REQUESTED: Draws the beautiful 3D hooded shadow pawn sliced from your sheet!
fun DrawScope.drawGamePawn(
    pawnsBitmap: ImageBitmap,
    playerColor: Color,
    x: Float,
    y: Float,
    size: Float,
    squashX: Float = 1f,
    stretchY: Float = 1f
) {
    val wImg = pawnsBitmap.width
    val hImg = pawnsBitmap.height
    
    val (srcX, srcY, srcW, srcH) = when {
        // Red (Ruby - Color(0xFFE50914))
        playerColor == Color(0xFFE50914) -> {
            val sW = wImg / 7
            val sH = hImg / 2
            FourTuple(0, 0, sW, sH)
        }
        // Orange (Amber - Color(0xFFFF7F00))
        playerColor == Color(0xFFFF7F00) -> {
            val sW = wImg / 7
            val sH = hImg / 2
            FourTuple(sW, 0, sW, sH)
        }
        // Yellow (Sunstone - Color(0xFFFFD700))
        playerColor == Color(0xFFFFD700) -> {
            val sW = wImg / 7
            val sH = hImg / 2
            FourTuple(2 * sW, 0, sW, sH)
        }
        // Green (Emerald - Color(0xFF00D26A))
        playerColor == Color(0xFF00D26A) -> {
            val sW = wImg / 7
            val sH = hImg / 2
            FourTuple(3 * sW, 0, sW, sH)
        }
        // Blue (Sapphire - Color(0xFF0070FF))
        playerColor == Color(0xFF0070FF) -> {
            val sW = wImg / 7
            val sH = hImg / 2
            FourTuple(4 * sW, 0, sW, sH)
        }
        // Purple (Amethyst - Color(0xFF8A2BE2))
        playerColor == Color(0xFF8A2BE2) -> {
            val sW = wImg / 7
            val sH = hImg / 2
            FourTuple(5 * sW, 0, sW, sH)
        }
        // Magenta (Rose - Color(0xFFFF007F))
        playerColor == Color(0xFFFF007F) -> {
            val sW = wImg / 7
            val sH = hImg / 2
            FourTuple(6 * sW, 0, sW, sH)
        }
        // Black (Obsidian - Color(0xFF222222))
        playerColor == Color(0xFF222222) -> {
            val sW = (wImg * 0.15f).toInt()
            val sH = hImg / 2
            val sX = (wImg * 0.28f).toInt()
            FourTuple(sX, sH, sW, sH)
        }
        // White (Silk - Color(0xFFF5F5F5))
        playerColor == Color(0xFFF5F5F5) -> {
            val sW = (wImg * 0.15f).toInt()
            val sH = hImg / 2
            val sX = (wImg * 0.53f).toInt()
            FourTuple(sX, sH, sW, sH)
        }
        // Default fallback (Red)
        else -> {
            val sW = wImg / 7
            val sH = hImg / 2
            FourTuple(0, 0, sW, sH)
        }
    }
    
    val dW = size * 1.5f * squashX
    val dH = size * 1.5f * stretchY
    val dx = x - (dW / 2f)
    val dy = y - (dH * 0.75f) // Offset up slightly so meeple sits perfectly on cap!

    drawImage(
        image = pawnsBitmap,
        srcOffset = IntOffset(srcX, srcY),
        srcSize = IntSize(srcW, srcH),
        dstOffset = IntOffset(dx.toInt(), dy.toInt()),
        dstSize = IntSize(dW.toInt(), dH.toInt())
    )
}

data class FourTuple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

// Draw elegant gold gilded corner filigrees
fun DrawScope.drawGildedCorner(x: Float, y: Float, isLeft: Boolean, isTop: Boolean) {
    val density = this.density
    val sizeVal = 42f * density
    val path = Path()
    val dx = if (isLeft) 1f else -1f
    val dy = if (isTop) 1f else -1f
    
    path.moveTo(x, y)
    path.lineTo(x + sizeVal * dx, y)
    path.quadraticBezierTo(x + sizeVal * dx * 0.4f, y + sizeVal * dy * 0.4f, x, y + sizeVal * dy)
    path.close()
    
    drawPath(path, color = Color(0xFFC5A059).copy(alpha = 0.2f))
    drawPath(path, color = Color(0xFFC5A059).copy(alpha = 0.6f), style = Stroke(width = 1.5f * density))
}
"""
with open(pawn_path, "w", encoding="utf-8") as f:
    f.write(pawn_content)
print("Wrote GamePawn.kt successfully!")

# 2. Write PillarsOfSummoning.kt cleanly
pillars_content = """package com.polylove.marble.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.polylove.marble.R
import com.polylove.marble.game.*
import com.polylove.marble.ui.CryptSkin
import com.polylove.marble.ui.theme.*

// REPLICATED: 2.5D Isometric Square Column/Pillar of Summoning drawing (T-RPG isometric perspective!)
// Uses your actual, photorealistic cracked basalt 3D column and glowing runes assets!
@Composable
fun SummoningPillar(
    tile: Tile,
    playersOnThisTile: List<Player>,
    isActivePlayerOnThisTile: Boolean,
    pulseScale: Float,
    tiltX: Float,
    tiltY: Float,
    skin: CryptSkin,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.density
    
    // Load the actual high-resolution asset images (PNGs with true transparency!)
    val pillarBitmap = ImageBitmap.imageResource(id = R.drawable.gothic_pillar)
    val runesBitmap = ImageBitmap.imageResource(id = R.drawable.gothic_runes)

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        
        // 1. DYNAMIC PARALLAX TILT - shifts the cap isometrically as the phone sways
        val tiltOffsetX = -tiltX * density * 0.4f
        val tiltOffsetY = tiltY * density * 0.4f
        
        // 2. SPRITE SHEET SLICING: Extract ONE of the 6 columns in the 3x2 pillar grid
        val pillarIdx = tile.index % 6
        val pillarCol = pillarIdx % 3
        val pillarRow = pillarIdx / 3
        
        val pW = pillarBitmap.width / 3
        val pH = pillarBitmap.height / 2
        
        val pSrcX = pillarCol * pW
        val pSrcY = pillarRow * pH
        
        // Draw the photorealistic sliced 3D basalt stone pillar centered inside the tile Box!
        drawImage(
            image = pillarBitmap,
            srcOffset = IntOffset(pSrcX, pSrcY),
            srcSize = IntSize(pW, pH),
            dstOffset = IntOffset(tiltOffsetX.toInt(), tiltOffsetY.toInt()),
            dstSize = IntSize(w.toInt(), h.toInt())
        )
        
        // 3. SPRITE SHEET SLICING: Extract a specific glowing rune from the 5x7 grid (35 runes total)
        // Slices the transparent PNG flawlessly without any white or black box overlaps!
        val (runeCol, runeRow) = when (tile.action) {
            TileAction.PUNISHMENT -> Pair(1, 2) // Gothic skull
            TileAction.SKIP_TURN -> Pair(4, 3) // Hourglass lock
            TileAction.MOVE_SPACES -> Pair(4, 1) // Spear displacement arrow
            TileAction.SWAP_POS -> Pair(4, 5) // Infinity cycle swap
            TileAction.ROLL_AGAIN -> Pair(1, 1) // Sparkling blessing star
            TileAction.BOARD_SHUFFLE -> Pair(4, 6) // Spiral chaos vortex
            else -> when (tile.cardCategory) {
                TileType.START -> Pair(2, 3) // Portal archway
                TileType.TRUTH -> Pair(1, 0) // Third-eye of whispers
                TileType.DARE -> Pair(0, 0) // Roaring flame of hellfire
                TileType.KINKY_LIGHT -> Pair(2, 0) // Crescent moon & star
                TileType.TEASE_DENIAL -> Pair(4, 4) // Winged cowl/horns of mask
                TileType.IMPACT_DOM -> Pair(3, 0) // Spikes trident of lash
                TileType.GROUP_BDSM -> Pair(3, 1) // Swirling cosmos vortex
                else -> Pair(1, 0) // Default whispers
            }
        }
        
        val rW = runesBitmap.width / 5
        val rH = runesBitmap.height / 7
        
        val rSrcX = runeCol * rW
        val rSrcY = runeRow * rH
        
        // Draw the glowing rune centered on the column cap face
        val scale = if (isActivePlayerOnThisTile) pulseScale else 1f
        val runeSizeW = (w * 0.38f * scale).toInt()
        val runeSizeH = (h * 0.38f * scale).toInt()
        
        // Center of the top cap shifted by 3D sways
        val capCenterX = (w / 2f + tiltOffsetX) - (runeSizeW / 2f)
        val capCenterY = (h * 0.28f + tiltOffsetY) - (runeSizeH / 2f) // Sit exactly on the upper slab top!
        
        // Draw central radial glow matching category under the rune
        val catColor = Color(android.graphics.Color.parseColor(tile.cardCategory.colorHex))
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(catColor.copy(alpha = 0.35f * scale), Color.Transparent),
                center = Offset(w / 2f + tiltOffsetX, h * 0.28f + tiltOffsetY),
                radius = w * 0.25f * scale
            ),
            radius = w * 0.25f * scale,
            center = Offset(w / 2f + tiltOffsetX, h * 0.28f + tiltOffsetY)
        )
        
        drawImage(
            image = runesBitmap,
            srcOffset = IntOffset(rSrcX, rSrcY),
            srcSize = IntSize(rW, rH),
            dstOffset = IntOffset(capCenterX.toInt(), capCenterY.toInt()),
            dstSize = IntSize(runeSizeW, runeSizeH)
        )
        
        // 4. Draw outer gold highlight outline if the active player stands on this column
        if (isActivePlayerOnThisTile) {
            drawCircle(
                color = BrassGold,
                radius = w * 0.44f * pulseScale,
                center = Offset(w / 2f + tiltOffsetX, h * 0.28f + tiltOffsetY),
                style = Stroke(width = 1.8f * density)
            )
        }
    }
}

// --- 🎲 EXCLUSIVE 3D DIE FACE PIXEL DRAWING ---
// UPDATED: Slices your brand-new transparent 3D dice sheet with glowing pink pips!
@Composable
fun DiceFace3D(value: Int, size: Int = 44) {
    val diceBitmap = ImageBitmap.imageResource(id = R.drawable.gothic_dice)
    
    Canvas(
        modifier = Modifier
            .size(size.dp)
            .shadow(6.dp, RoundedCornerShape(10.dp))
    ) {
        val w = this.size.width
        val h = this.size.height
        
        // 3x2 grid of dice faces:
        // Value 1, 2, 3 on Row 0
        // Value 4, 5, 6 on Row 1
        val idx = (value - 1).coerceIn(0, 5)
        val col = idx % 3
        val row = idx / 3
        
        val dW = diceBitmap.width / 3
        val dH = diceBitmap.height / 2
        
        val srcX = col * dW
        val srcY = row * dH
        
        drawImage(
            image = diceBitmap,
            srcOffset = IntOffset(srcX, srcY),
            srcSize = IntSize(dW, dH),
            dstOffset = IntOffset(0, 0),
            dstSize = IntSize(w.toInt(), h.toInt())
        )
    }
}
"""
with open(pillars_path, "w", encoding="utf-8") as f:
    f.write(pillars_content)
print("Wrote PillarsOfSummoning.kt cleanly!")

# 3. Touch AndroidManifest.xml
with open(manifest_path, "r", encoding="utf-8") as f:
    manifest = f.read()
manifest = manifest.replace("</manifest>", "   </manifest>")
with open(manifest_path, "w", encoding="utf-8") as f:
    f.write(manifest)
print("AndroidManifest touched!")

print("--- RUNNING COMPILE DIRECTLY ---")
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
    print("SUCCESSFUL BUILD! Copying APK...")
    subprocess.run(["cp", "PolyLoveMarble/app/build/outputs/apk/debug/app-debug.apk", "/home/user/PolyKinkMarble.apk"])
    print("Master APK compiled successfully!")
else:
    print(f"FAILED MASTER COMPILE! Code: {result.returncode}")
    exit(1)
