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
import com.polylove.marble.ui.CryptSkin
import com.polylove.marble.ui.theme.*

// Imports needed for pre-sliced Image-based Dice
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

// 2.5D Isometric Square Column/Pillar of Summoning drawing
// FIXED: Scale reduced from 3x to proper proportions to prevent tile overlap
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

    // Load the actual high-resolution asset images
    val pillarBitmap = ImageBitmap.imageResource(id = R.drawable.gothic_pillar)
    val runesBitmap = ImageBitmap.imageResource(id = R.drawable.gothic_runes)

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. DYNAMIC PARALLAX TILT - shifts the cap isometrically as the phone sways
        // Reduced tilt multiplier to prevent excessive displacement
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

        // FIXED: Properly proportioned pillar scaling
        // Width fills 95% of cell (tiny gap between columns for visual separation)
        // Height extends 1.7x cell height to give nice 3D column depth
        val targetW = w * 0.95f
        val targetH = h * 1.7f

        // Center the pillar horizontally inside the cell
        val drawX = (w - targetW) / 2f + tiltOffsetX

        // Anchor the bottom of the pillar at the bottom of the cell
        // The column extends upward beyond the cell top (that's the 3D height)
        val drawY = h - targetH + tiltOffsetY

        // Draw the photorealistic sliced 3D basalt stone pillar
        drawImage(
            image = pillarBitmap,
            srcOffset = IntOffset(pSrcX, pSrcY),
            srcSize = IntSize(pW, pH),
            dstOffset = IntOffset(drawX.toInt(), drawY.toInt()),
            dstSize = IntSize(targetW.toInt(), targetH.toInt())
        )

        // 3. SPRITE SHEET SLICING: Extract a specific glowing rune from the 5x7 grid
        val (runeCol, runeRow) = when (tile.action) {
            TileAction.PUNISHMENT -> Pair(1, 2)
            TileAction.SKIP_TURN -> Pair(4, 3)
            TileAction.MOVE_SPACES -> Pair(4, 1)
            TileAction.SWAP_POS -> Pair(4, 5)
            TileAction.ROLL_AGAIN -> Pair(1, 1)
            TileAction.BOARD_SHUFFLE -> Pair(4, 6)
            else -> when (tile.cardCategory) {
                TileType.START -> Pair(2, 3)
                TileType.TRUTH -> Pair(1, 0)
                TileType.DARE -> Pair(0, 0)
                TileType.KINKY_LIGHT -> Pair(2, 0)
                TileType.TEASE_DENIAL -> Pair(4, 4)
                TileType.IMPACT_DOM -> Pair(3, 0)
                TileType.GROUP_BDSM -> Pair(3, 1)
                else -> Pair(1, 0)
            }
        }

        val rW = runesBitmap.width / 5
        val rH = runesBitmap.height / 7

        val rSrcX = runeCol * rW
        val rSrcY = runeRow * rH

        // Center of the top cap face - adjusted for new 1.7x height
        // The cap face is at roughly 25% from the top of the pillar sprite
        val targetCapCenterX = drawX + targetW * 0.5f
        val targetCapCenterY = drawY + targetH * 0.25f

        // Draw the glowing rune centered on the column cap face
        val scale = if (isActivePlayerOnThisTile) pulseScale else 1f
        val runeSizeW = (targetW * 0.28f * scale).toInt()
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
                color = BrassGold,
                radius = targetW * 0.22f * pulseScale,
                center = Offset(targetCapCenterX, targetCapCenterY),
                style = Stroke(width = 2f * density)
            )
        }
    }
}

// --- 🎲 3D DIE FACE PIXEL DRAWING ---
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
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}
