package com.polylove.marble.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.polylove.marble.game.*
import com.polylove.marble.ui.theme.*

// --- 🔒 EXCLUSIVE GAME-ICONS.NET VECTOR REPLICATIONS (100% OFFLINE, NO MESSY LINES) ---

fun DrawScope.drawPadlockVector(isClosed: Boolean, color: Color) {
    val w = size.width
    val h = size.height
    
    val shacklePath = Path()
    if (isClosed) {
        shacklePath.moveTo(w * 0.35f, h * 0.5f)
        shacklePath.lineTo(w * 0.35f, h * 0.32f)
        shacklePath.quadraticBezierTo(w * 0.5f, h * 0.1f, w * 0.65f, h * 0.32f)
        shacklePath.lineTo(w * 0.65f, h * 0.5f)
    } else {
        shacklePath.moveTo(w * 0.35f, h * 0.5f)
        shacklePath.lineTo(w * 0.35f, h * 0.32f)
        shacklePath.quadraticBezierTo(w * 0.48f, h * 0.08f, w * 0.62f, h * 0.28f)
        shacklePath.lineTo(w * 0.74f, h * 0.44f)
    }
    
    drawPath(
        path = shacklePath,
        color = color,
        style = Stroke(width = h * 0.11f, cap = StrokeCap.Round)
    )
    
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.22f, h * 0.46f),
        size = Size(w * 0.56f, h * 0.44f),
        cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
    )
    
    drawCircle(
        color = ObsidianBlack,
        radius = h * 0.07f,
        center = Offset(w * 0.5f, h * 0.64f)
    )
    val trianglePath = Path().apply {
        moveTo(w * 0.46f, h * 0.64f)
        lineTo(w * 0.54f, h * 0.64f)
        lineTo(w * 0.56f, h * 0.82f)
        lineTo(w * 0.44f, h * 0.82f)
        close()
    }
    drawPath(trianglePath, color = ObsidianBlack)
}

fun DrawScope.drawHandcuffsVector(color: Color) {
    val w = size.width
    val h = size.height
    
    drawCircle(
        color = color,
        radius = w * 0.2f,
        center = Offset(w * 0.32f, h * 0.45f),
        style = Stroke(width = w * 0.06f)
    )
    drawCircle(
        color = color,
        radius = w * 0.2f,
        center = Offset(w * 0.68f, h * 0.55f),
        style = Stroke(width = w * 0.06f)
    )
    
    val linkPath = Path().apply {
        moveTo(w * 0.42f, h * 0.45f)
        quadraticBezierTo(w * 0.5f, h * 0.52f, w * 0.58f, h * 0.45f)
    }
    drawPath(
        path = linkPath,
        color = color,
        style = Stroke(width = w * 0.05f, cap = StrokeCap.Round)
    )
}

fun DrawScope.drawWhipVector(color: Color) {
    val w = size.width
    val h = size.height
    
    val handlePath = Path().apply {
        moveTo(w * 0.15f, h * 0.85f)
        lineTo(w * 0.45f, h * 0.55f)
    }
    drawPath(handlePath, color = color, style = Stroke(width = h * 0.1f, cap = StrokeCap.Round))
    
    val lash1 = Path().apply {
        moveTo(w * 0.42f, h * 0.52f)
        cubicTo(w * 0.65f, h * 0.25f, w * 0.45f, h * 0.2f, w * 0.85f, h * 0.15f)
    }
    val lash2 = Path().apply {
        moveTo(w * 0.44f, h * 0.54f)
        cubicTo(w * 0.75f, h * 0.45f, w * 0.55f, h * 0.15f, w * 0.82f, h * 0.35f)
    }
    
    drawPath(lash1, color = color, style = Stroke(width = h * 0.04f, cap = StrokeCap.Round))
    drawPath(lash2, color = color.copy(alpha = 0.8f), style = Stroke(width = h * 0.035f, cap = StrokeCap.Round))
}

fun DrawScope.drawMaskVector(color: Color) {
    val w = size.width
    val h = size.height
    
    val maskPath = Path().apply {
        moveTo(w * 0.1f, h * 0.4f)
        cubicTo(w * 0.15f, h * 0.15f, w * 0.4f, h * 0.2f, w * 0.5f, h * 0.45f)
        cubicTo(w * 0.60f, h * 0.2f, w * 0.85f, h * 0.15f, w * 0.9f, h * 0.4f)
        cubicTo(w * 0.95f, h * 0.65f, w * 0.65f, h * 0.75f, w * 0.5f, h * 0.58f)
        cubicTo(w * 0.35f, h * 0.75f, w * 0.05f, h * 0.65f, w * 0.1f, h * 0.4f)
        close()
    }
    drawPath(maskPath, color = color)
    
    drawCircle(color = ObsidianBlack, radius = w * 0.07f, center = Offset(w * 0.32f, h * 0.42f))
    drawCircle(color = ObsidianBlack, radius = w * 0.07f, center = Offset(w * 0.68f, h * 0.42f))
}

fun DrawScope.drawSpeechVector(color: Color) {
    val w = size.width
    val h = size.height
    
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.1f, h * 0.18f),
        size = Size(w * 0.8f, h * 0.52f),
        cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
    )
    val tailPath = Path().apply {
        moveTo(w * 0.25f, h * 0.68f)
        lineTo(w * 0.15f, h * 0.85f)
        lineTo(w * 0.38f, h * 0.68f)
        close()
    }
    drawPath(tailPath, color = color)
}

fun DrawScope.drawFlameVector(color: Color) {
    val w = size.width
    val h = size.height
    
    val flamePath = Path().apply {
        moveTo(w * 0.5f, h * 0.05f)
        cubicTo(w * 0.32f, h * 0.32f, w * 0.18f, h * 0.55f, w * 0.22f, h * 0.78f)
        quadraticBezierTo(w * 0.25f, h * 0.95f, w * 0.5f, h * 0.95f)
        quadraticBezierTo(w * 0.75f, h * 0.95f, w * 0.78f, h * 0.78f)
        cubicTo(w * 0.82f, h * 0.55f, w * 0.68f, h * 0.32f, w * 0.5f, h * 0.05f)
        close()
    }
    drawPath(flamePath, color = color)
}

fun DrawScope.drawSparkleVector(color: Color) {
    val w = size.width
    val h = size.height
    
    val spark = Path().apply {
        moveTo(w * 0.5f, 0f)
        quadraticBezierTo(w * 0.5f, h * 0.5f, w, h * 0.5f)
        quadraticBezierTo(w * 0.5f, h * 0.5f, w * 0.5f, h)
        quadraticBezierTo(w * 0.5f, h * 0.5f, 0f, h * 0.5f)
        quadraticBezierTo(w * 0.5f, h * 0.5f, w * 0.5f, 0f)
        close()
    }
    drawPath(spark, color = color)
}

fun DrawScope.drawShieldVector(color: Color) {
    val w = size.width
    val h = size.height
    
    val shieldPath = Path().apply {
        moveTo(w * 0.15f, h * 0.15f)
        lineTo(w * 0.85f, h * 0.15f)
        lineTo(w * 0.85f, h * 0.5f)
        quadraticBezierTo(w * 0.85f, h * 0.85f, w * 0.5f, h * 0.95f)
        quadraticBezierTo(w * 0.15f, h * 0.85f, w * 0.15f, h * 0.5f)
        close()
    }
    drawPath(shieldPath, color = color)
    
    drawLine(
        color = ObsidianBlack,
        start = Offset(w * 0.5f, h * 0.32f),
        end = Offset(w * 0.5f, h * 0.58f),
        strokeWidth = h * 0.08f,
        cap = StrokeCap.Round
    )
    drawCircle(
        color = ObsidianBlack,
        radius = h * 0.05f,
        center = Offset(w * 0.5f, h * 0.72f)
    )
}

fun DrawScope.drawSwapVector(color: Color) {
    val w = size.width
    val h = size.height
    
    drawArc(
        color = color,
        startAngle = 0f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(w * 0.15f, h * 0.15f),
        size = Size(w * 0.7f, h * 0.7f),
        style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
    )
    drawArc(
        color = color,
        startAngle = 180f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(w * 0.15f, h * 0.15f),
        size = Size(w * 0.7f, h * 0.7f),
        style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
    )
}

fun DrawScope.drawDoubleDiceVector(color: Color) {
    val w = size.width
    val h = size.height
    
    drawRoundRect(
        color = color.copy(alpha = 0.7f),
        topLeft = Offset(w * 0.1f, h * 0.1f),
        size = Size(w * 0.5f, h * 0.5f),
        cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
    )
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.4f, h * 0.4f),
        size = Size(w * 0.5f, h * 0.5f),
        cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
    )
}

fun DrawScope.drawChaosVector(color: Color) {
    val w = size.width
    val h = size.height
    
    val stand = Path().apply {
        moveTo(w * 0.3f, h * 0.85f)
        lineTo(w * 0.5f, h * 0.7f)
        lineTo(w * 0.7f, h * 0.85f)
    }
    drawPath(stand, color = SteelGrey, style = Stroke(width = w * 0.08f, cap = StrokeCap.Round))
    
    drawCircle(
        color = color,
        radius = w * 0.32f,
        center = Offset(w * 0.5f, h * 0.42f)
    )
}

fun DrawScope.drawGroupVector(color: Color) {
    val w = size.width
    val h = size.height
    
    val crown = Path().apply {
        moveTo(w * 0.15f, h * 0.78f)
        lineTo(w * 0.1f, h * 0.35f)
        lineTo(w * 0.32f, h * 0.55f)
        lineTo(w * 0.5f, h * 0.22f)
        lineTo(w * 0.68f, h * 0.55f)
        lineTo(w * 0.9f, h * 0.35f)
        lineTo(w * 0.85f, h * 0.78f)
        close()
    }
    drawPath(crown, color = color)
}

@Composable
fun TileVectorIcon(type: TileType, action: TileAction, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val color = Color(android.graphics.Color.parseColor(type.colorHex))
        when (action) {
            TileAction.PUNISHMENT -> drawShieldVector(Color(0xFFFF007F))
            TileAction.SKIP_TURN -> drawRestraintIconVector(WhipBrown)
            TileAction.MOVE_SPACES -> drawWhipVector(Color(0xFFFFB703))
            TileAction.SWAP_POS -> drawSwapVector(Color(0xFF2EC4B6))
            TileAction.ROLL_AGAIN -> drawDoubleDiceVector(BrassGold)
            TileAction.BOARD_SHUFFLE -> drawChaosVector(Color(0xFF00FFCC))
            else -> when (type) {
                TileType.START -> drawPadlockVector(true, LatexCrimson)
                TileType.TRUTH -> drawSpeechVector(color)
                TileType.DARE -> drawFlameVector(color)
                TileType.KINKY_LIGHT -> drawSparkleVector(color)
                TileType.TEASE_DENIAL -> drawMaskVector(color)
                TileType.IMPACT_DOM -> drawHandcuffsVector(color)
                TileType.GROUP_BDSM -> drawGroupVector(color)
                else -> drawPadlockVector(true, color)
            }
        }
    }
}

fun DrawScope.drawRestraintIconVector(color: Color) {
    val w = size.width
    val h = size.height
    drawCircle(
        color = color,
        radius = w * 0.28f,
        center = Offset(w * 0.5f, h * 0.5f),
        style = Stroke(width = w * 0.08f)
    )
    drawRect(
        color = BrassGold,
        topLeft = Offset(w * 0.42f, h * 0.65f),
        size = Size(w * 0.16f, h * 0.18f)
    )
}
