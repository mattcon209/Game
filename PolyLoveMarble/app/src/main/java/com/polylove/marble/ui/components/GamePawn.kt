package com.polylove.marble.ui.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.polylove.marble.ui.theme.GoldPrimary

// EXCLUSIVE REQUESTED: Draw a beautiful, physical-looking 3D Board Game Pawn/Meeple (Perfect wide meeple proportions, no more squishing!)
fun DrawScope.drawGamePawn(color: Color, x: Float, y: Float, size: Float, squashX: Float = 1f, stretchY: Float = 1f) {
    val h = size * stretchY
    val w = size * 0.9f * squashX // Wider proportions (increased from 0.7f to 0.9f)
    val density = this.density
    
    // 1. Pawn soft physical floor drop-shadow (Compensates for stretch!)
    drawOval(
        color = Color.Black.copy(alpha = 0.5f),
        topLeft = Offset(x - w * 0.5f, y + h * 0.35f),
        size = Size(w, h * 0.2f)
    )
    
    // 2. Base stand (rounded heavy base)
    drawRoundRect(
        color = color,
        topLeft = Offset(x - w * 0.5f, y + h * 0.15f),
        size = Size(w, h * 0.25f),
        cornerRadius = CornerRadius(w * 0.2f, w * 0.2f)
    )
    // Base gold ring highlight
    drawRoundRect(
        color = GoldPrimary,
        topLeft = Offset(x - w * 0.45f, y + h * 0.15f),
        size = Size(w * 0.9f, h * 0.05f),
        cornerRadius = CornerRadius(w * 0.2f, w * 0.2f),
        style = Stroke(width = 1.2f * density)
    )
    
    // 2.5 Occult/Succubus shoulder bat-wings (Vampire theme!)
    val leftWing = Path().apply {
        moveTo(x - w * 0.2f, y - h * 0.05f)
        cubicTo(x - w * 0.9f, y - h * 0.25f, x - w * 0.7f, y + h * 0.2f, x - w * 0.15f, y + h * 0.15f)
        close()
    }
    val rightWing = Path().apply {
        moveTo(x + w * 0.2f, y - h * 0.05f)
        cubicTo(x + w * 0.9f, y - h * 0.25f, x + w * 0.7f, y + h * 0.2f, x + w * 0.15f, y + h * 0.15f)
        close()
    }
    drawPath(leftWing, color = color.copy(alpha = 0.85f))
    drawPath(rightWing, color = color.copy(alpha = 0.85f))
    drawPath(leftWing, color = GoldPrimary.copy(alpha = 0.5f), style = Stroke(width = 1f * density))
    drawPath(rightWing, color = GoldPrimary.copy(alpha = 0.5f), style = Stroke(width = 1f * density))
    
    // 3. Pawn flared body path (wider meeple shoulders)
    val bodyPath = Path().apply {
        moveTo(x - w * 0.4f, y + h * 0.18f)
        quadraticBezierTo(x - w * 0.15f, y - h * 0.12f, x - w * 0.2f, y - h * 0.22f)
        lineTo(x + w * 0.2f, y - h * 0.22f)
        quadraticBezierTo(x + w * 0.15f, y - h * 0.12f, x + w * 0.4f, y + h * 0.18f)
        close()
    }
    drawPath(bodyPath, color = color)
    drawPath(
        bodyPath,
        brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.22f), Color.Transparent),
            center = Offset(x, y - h * 0.05f),
            radius = w * 0.5f
        )
    )
    
    // 4. Pawn neck ring (Collar buckle!)
    drawRoundRect(
        color = GoldPrimary,
        topLeft = Offset(x - w * 0.22f, y - h * 0.25f),
        size = Size(w * 0.44f, h * 0.05f),
        cornerRadius = CornerRadius(1f, 1f)
    )
    
    // 5. Pawn head (larger circle)
    drawCircle(
        color = color,
        radius = w * 0.32f,
        center = Offset(x, y - h * 0.45f)
    )
    // Head reflection highlight
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.35f), Color.Transparent),
            center = Offset(x - w * 0.1f, y - h * 0.52f),
            radius = w * 0.22f
        ),
        radius = w * 0.32f,
        center = Offset(x, y - h * 0.45f)
    )
}

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
    
    drawPath(path, color = GoldPrimary.copy(alpha = 0.2f))
    drawPath(path, color = GoldPrimary.copy(alpha = 0.6f), style = Stroke(width = 1.5f * density))
}
