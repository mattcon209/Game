package com.polylove.marble.ui.components

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
import com.polylove.marble.ui.theme.*

// Imports needed for pre-sliced Image-based Dice
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

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
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.density
    
    // Load the actual high-resolution asset images
    val pillarBitmap = ImageBitmap.imageResource(id = R.drawable.gothic_pillar)
    val runesBitmap = ImageBitmap.imageResource(id = R.drawable.gothic_runes)

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        
        // 1. DYNAMIC PARALLAX TILT - shifts the cap isometrically as the phone sways
        val tiltOffsetX = -tiltX * density * 0.15f
        val tiltOffsetY = tiltY * density * 0.15f
        
        // 2. SPRITE SHEET SLICING: Extract ONE of the 6 columns in the 3x2 pillar grid
        val pillarIdx = tile.index % 6
        val pillarCol = pillarIdx % 3
        val pillarRow = pillarIdx / 3
        
        val pW = pillarBitmap.width / 3
        val pH = pillarBitmap.height / 2
        
        val pSrcX = pillarCol * pW
        val pSrcY = pillarRow * pH
        
        // Pillars are made 3x larger as requested!
        val targetW = w * 0.95f
        val targetH = h * 1.7f
        
        // Center the scaled pillar horizontally inside the cell:
        val drawX = (w - targetW) / 2f + tiltOffsetX
        
        // Place the bottom of the pillar at the bottom of the cell (or slightly lower):
        val drawY = h - targetH + tiltOffsetY
        
        // Draw the photorealistic sliced 3D basalt stone pillar centered inside the tile Box!
        drawImage(
            image = pillarBitmap,
            srcOffset = IntOffset(pSrcX, pSrcY),
            srcSize = IntSize(pW, pH),
            dstOffset = IntOffset(drawX.toInt(), drawY.toInt()),
            dstSize = IntSize(targetW.toInt(), targetH.toInt())
        )
        
        // 3. SPRITE SHEET SLICING: Extract a specific glowing rune from the 5x7 grid (35 runes total)
        // We map each game action and category to a beautiful tailored rune on the sheet!
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
        
        // Center of the top cap shifted by 3D sways
        val targetCapCenterX = drawX + targetW * 0.5f
        val targetCapCenterY = drawY + targetH * 0.25f
        
        // Draw the glowing rune centered on the column cap face
        val scale = if (isActivePlayerOnThisTile) pulseScale else 1f
        val runeSizeW = (targetW * 0.28f * scale).toInt() // Adjusted from 0.38f to match the 3x larger pillar!
        val runeSizeH = (targetH * 0.16f * scale).toInt()
        
        val runeX = targetCapCenterX - (runeSizeW / 2f)
        val runeY = targetCapCenterY - (runeSizeH / 2f)
        
        // Draw central radial glow matching category under the rune
        val catColor = Color(android.graphics.Color.parseColor(tile.cardCategory.colorHex))
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(catColor.copy(alpha = 0.35f * scale), Color.Transparent),
                center = Offset(targetCapCenterX, targetCapCenterY),
                radius = targetW * 0.18f * scale
            ),
            radius = targetW * 0.18f * scale,
            center = Offset(targetCapCenterX, targetCapCenterY)
        )
        
        drawImage(
            image = runesBitmap,
            srcOffset = IntOffset(rSrcX, rSrcY),
            srcSize = IntSize(rW, rH),
            dstOffset = IntOffset(runeX.toInt(), runeY.toInt()),
            dstSize = IntSize(runeSizeW, runeSizeH)
        )
        
        // 4. Draw outer gold highlight outline if the active player stands on this column
        if (isActivePlayerOnThisTile) {
            drawCircle(
                color = GoldPrimary,
                radius = targetW * 0.22f * pulseScale, // Adjusted to match the 3x larger pillar!
                center = Offset(targetCapCenterX, targetCapCenterY),
                style = Stroke(width = 1.8f * density)
            )
        }
    }
}

// --- 🎲 EXCLUSIVE 3D DIE FACE PIXEL DRAWING ---
@Composable
fun DiceFace3D(value: Int, size: Int = 44) {
    val resId = when (value) {
        1 -> R.drawable.gothic_dice_1
        2 -> R.drawable.gothic_dice_2
        3 -> R.drawable.gothic_dice_3
        4 -> R.drawable.gothic_dice_4
        5 -> R.drawable.gothic_dice_5
        6 -> R.drawable.gothic_dice_6
        else -> R.drawable.gothic_dice_1
    }
    
    Box(
        modifier = Modifier
            .size(size.dp)
            .shadow(6.dp, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = "Dice Face $value",
            contentScale = ContentScale.Fit, // Centers and fits perfectly inside the box, ignoring outer space
            modifier = Modifier.fillMaxSize()
        )
    }
}
