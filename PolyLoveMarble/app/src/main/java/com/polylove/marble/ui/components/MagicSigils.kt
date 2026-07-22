package com.polylove.marble.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.polylove.marble.ui.theme.*

@Composable
fun MagicCircleSigil(
    variant: String,
    modifier: Modifier = Modifier,
    color: Color = CrimsonGlow,
    size: Int = 32
) {
    Canvas(modifier = modifier.size(size.dp)) {
        val cx = this.size.width / 2f
        val cy = this.size.height / 2f
        val r = minOf(this.size.width, this.size.height) / 2f - 2f
        val density = this.density

        when (variant) {
            "TRUTH" -> drawTruthSigil(cx, cy, r, color, density)
            "DARE" -> drawDareSigil(cx, cy, r, color, density)
            "KINKY_LIGHT" -> drawKinkyLightSigil(cx, cy, r, color, density)
            "TEASE_DENIAL" -> drawTeaseDenialSigil(cx, cy, r, color, density)
            "IMPACT_DOM" -> drawImpactDomSigil(cx, cy, r, color, density)
            "GROUP_BDSM" -> drawGroupBDSMSigil(cx, cy, r, color, density)
            "PUNISHMENT" -> drawPunishmentSigil(cx, cy, r, color, density)
            "SKIP_TURN" -> drawSkipTurnSigil(cx, cy, r, color, density)
            "MOVE_SPACES" -> drawMoveSpacesSigil(cx, cy, r, color, density)
            "SWAP_POS" -> drawSwapPosSigil(cx, cy, r, color, density)
            "ROLL_AGAIN" -> drawRollAgainSigil(cx, cy, r, color, density)
            "BOARD_SHUFFLE" -> drawBoardShuffleSigil(cx, cy, r, color, density)
            "NORMAL_CARD" -> drawNormalCardSigil(cx, cy, r, color, density)
            "DOUBLE_DARE" -> drawDoubleDareSigil(cx, cy, r, color, density)
            "CUSTOM_MESSAGE" -> drawCustomMessageSigil(cx, cy, r, color, density)
            "START" -> drawStartSigil(cx, cy, r, color, density)
            "CURSE" -> drawCurseSigil(cx, cy, r, color, density)
            else -> drawDefaultSigil(cx, cy, r, color, density)
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTruthSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    drawCircle(color, r * 0.7f, Offset(cx, cy), style = Stroke(width = 1f * density))
    drawCircle(color, r * 0.3f, Offset(cx, cy))
    drawLine(color, Offset(cx - r, cy), Offset(cx + r, cy), strokeWidth = 0.8f * density)
    drawLine(color, Offset(cx, cy - r), Offset(cx, cy + r), strokeWidth = 0.8f * density)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDareSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    val flamePath = Path().apply {
        moveTo(cx, cy - r * 0.6f)
        cubicTo(cx + r * 0.3f, cy - r * 0.2f, cx + r * 0.25f, cy + r * 0.3f)
        quadraticBezierTo(cx + r * 0.1f, cy + r * 0.6f, cx, cy + r * 0.6f)
        quadraticBezierTo(cx - r * 0.1f, cy + r * 0.6f, cx - r * 0.25f, cy + r * 0.3f)
        cubicTo(cx - r * 0.3f, cy - r * 0.2f, cx, cy - r * 0.6f)
        close()
    }
    drawPath(flamePath, color)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawKinkyLightSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    val starPath = Path().apply {
        moveTo(cx, cy - r * 0.7f)
        quadraticBezierTo(cx + r * 0.2f, cy, cx + r * 0.7f, cy)
        quadraticBezierTo(cx, cy + r * 0.2f, cx, cy + r * 0.7f)
        quadraticBezierTo(cx - r * 0.2f, cy, cx - r * 0.7f, cy)
        quadraticBezierTo(cx, cy - r * 0.2f, cx, cy - r * 0.7f)
        close()
    }
    drawPath(starPath, color)
    drawCircle(color.copy(alpha = 0.5f), r * 0.2f, Offset(cx, cy))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTeaseDenialSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    drawRect(color, Offset(cx - r * 0.6f, cy - r * 0.15f), androidx.compose.ui.geometry.Size(r * 1.2f, r * 0.3f))
    drawCircle(ObsidianBlack, r * 0.15f, Offset(cx - r * 0.25f, cy))
    drawCircle(ObsidianBlack, r * 0.15f, Offset(cx + r * 0.25f, cy))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawImpactDomSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    val whipPath = Path().apply {
        moveTo(cx + r * 0.3f, cy + r * 0.5f)
        cubicTo(cx + r * 0.1f, cy + r * 0.1f, cx - r * 0.3f, cy - r * 0.2f, cx - r * 0.6f, cy - r * 0.5f)
    }
    drawPath(whipPath, color, style = Stroke(width = 2f * density, cap = StrokeCap.Round))
    drawCircle(color, r * 0.1f, Offset(cx + r * 0.3f, cy + r * 0.5f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawGroupBDSMSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    drawCircle(color, r * 0.35f, Offset(cx, cy - r * 0.3f), style = Stroke(width = 1f * density))
    drawCircle(color, r * 0.35f, Offset(cx - r * 0.3f, cy + r * 0.2f), style = Stroke(width = 1f * density))
    drawCircle(color, r * 0.35f, Offset(cx + r * 0.3f, cy + r * 0.2f), style = Stroke(width = 1f * density))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPunishmentSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    val shieldPath = Path().apply {
        moveTo(cx, cy - r * 0.6f)
        lineTo(cx + r * 0.5f, cy - r * 0.3f)
        lineTo(cx + r * 0.5f, cy + r * 0.2f)
        quadraticBezierTo(cx + r * 0.5f, cy + r * 0.6f, cx, cy + r * 0.7f)
        quadraticBezierTo(cx - r * 0.5f, cy + r * 0.6f, cx - r * 0.5f, cy + r * 0.2f)
        lineTo(cx - r * 0.5f, cy - r * 0.3f)
        close()
    }
    drawPath(shieldPath, color, style = Stroke(width = 1.2f * density))
    drawLine(color, Offset(cx - r * 0.25f, cy - r * 0.2f), Offset(cx + r * 0.25f, cy + r * 0.3f), strokeWidth = 1.5f * density)
    drawLine(color, Offset(cx + r * 0.25f, cy - r * 0.2f), Offset(cx - r * 0.25f, cy + r * 0.3f), strokeWidth = 1.5f * density)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSkipTurnSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    drawCircle(color, r * 0.5f, Offset(cx, cy), style = Stroke(width = 1f * density))
    drawLine(color, Offset(cx - r * 0.6f, cy - r * 0.6f), Offset(cx + r * 0.6f, cy + r * 0.6f), strokeWidth = 2f * density)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMoveSpacesSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    val arrowPath = Path().apply {
        moveTo(cx - r * 0.4f, cy)
        lineTo(cx + r * 0.2f, cy)
        moveTo(cx + r * 0.0f, cy - r * 0.25f)
        lineTo(cx + r * 0.2f, cy)
        lineTo(cx + r * 0.0f, cy + r * 0.25f)
    }
    drawPath(arrowPath, color, style = Stroke(width = 1.5f * density, cap = StrokeCap.Round))
    drawArc(color, startAngle = 200f, sweepAngle = 140f, useCenter = false, topLeft = Offset(cx - r * 0.5f, cy - r * 0.5f), size = androidx.compose.ui.geometry.Size(r, r), style = Stroke(width = 1f * density, cap = StrokeCap.Round))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSwapPosSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    drawArc(color, startAngle = 0f, sweepAngle = 140f, useCenter = false, topLeft = Offset(cx - r * 0.5f, cy - r * 0.5f), size = androidx.compose.ui.geometry.Size(r, r), style = Stroke(width = 1.2f * density, cap = StrokeCap.Round))
    drawArc(color, startAngle = 180f, sweepAngle = 140f, useCenter = false, topLeft = Offset(cx - r * 0.5f, cy - r * 0.5f), size = androidx.compose.ui.geometry.Size(r, r), style = Stroke(width = 1.2f * density, cap = StrokeCap.Round))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRollAgainSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    drawRoundRect(color.copy(alpha = 0.7f), Offset(cx - r * 0.35f, cy - r * 0.35f), androidx.compose.ui.geometry.Size(r * 0.4f, r * 0.4f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f * density))
    drawRoundRect(color, Offset(cx - r * 0.05f, cy - r * 0.05f), androidx.compose.ui.geometry.Size(r * 0.4f, r * 0.4f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f * density))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBoardShuffleSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    drawCircle(color, r * 0.4f, Offset(cx, cy - r * 0.1f))
    drawPath(Path().apply {
        moveTo(cx - r * 0.2f, cy + r * 0.3f)
        lineTo(cx, cy + r * 0.5f)
        lineTo(cx + r * 0.2f, cy + r * 0.3f)
    }, color, style = Stroke(width = 1f * density, cap = StrokeCap.Round))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawNormalCardSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    drawRoundRect(color, Offset(cx - r * 0.35f, cy - r * 0.4f), androidx.compose.ui.geometry.Size(r * 0.7f, r * 0.8f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f * density), style = Stroke(width = 1f * density))
    drawLine(color, Offset(cx - r * 0.2f, cy - r * 0.2f), Offset(cx + r * 0.2f, cy - r * 0.2f), strokeWidth = 0.8f * density)
    drawLine(color, Offset(cx - r * 0.2f, cy), Offset(cx + r * 0.2f, cy), strokeWidth = 0.8f * density)
    drawLine(color, Offset(cx - r * 0.2f, cy + r * 0.2f), Offset(cx + r * 0.1f, cy + r * 0.2f), strokeWidth = 0.8f * density)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDoubleDareSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    val flame1 = Path().apply {
        moveTo(cx - r * 0.15f, cy + r * 0.4f)
        cubicTo(cx - r * 0.05f, cy, cx + r * 0.05f, cy - r * 0.3f, cx + r * 0.1f, cy + r * 0.4f)
        close()
    }
    val flame2 = Path().apply {
        moveTo(cx + r * 0.15f, cy + r * 0.4f)
        cubicTo(cx + r * 0.25f, cy, cx + r * 0.35f, cy - r * 0.3f, cx + r * 0.3f, cy + r * 0.4f)
        close()
    }
    drawPath(flame1, color)
    drawPath(flame2, color)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCustomMessageSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    val bubblePath = Path().apply {
        addRoundRect(androidx.compose.ui.geometry.RoundRect(
            androidx.compose.ui.geometry.Rect(cx - r * 0.4f, cy - r * 0.3f, cx + r * 0.4f, cy + r * 0.2f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.1f)
        ))
        moveTo(cx - r * 0.1f, cy + r * 0.2f)
        lineTo(cx + r * 0.1f, cy + r * 0.2f)
        lineTo(cx, cy + r * 0.4f)
        close()
    }
    drawPath(bubblePath, color)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStartSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    drawCircle(color, r * 0.6f, Offset(cx, cy), style = Stroke(width = 1f * density))
    drawCircle(color, r * 0.25f, Offset(cx, cy))
    drawRoundRect(color, Offset(cx - r * 0.12f, cy - r * 0.05f), androidx.compose.ui.geometry.Size(r * 0.24f, r * 0.2f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(1f * density))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCurseSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    drawCircle(color, r * 0.4f, Offset(cx, cy - r * 0.1f), style = Stroke(width = 1f * density))
    drawRect(color, Offset(cx - r * 0.2f, cy + r * 0.2f), androidx.compose.ui.geometry.Size(r * 0.4f, r * 0.15f))
    drawCircle(color, r * 0.08f, Offset(cx - r * 0.12f, cy - r * 0.1f))
    drawCircle(color, r * 0.08f, Offset(cx + r * 0.12f, cy - r * 0.1f))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDefaultSigil(cx: Float, cy: Float, r: Float, color: Color, density: Float) {
    drawCircle(color.copy(alpha = 0.3f), r, Offset(cx, cy), style = Stroke(width = 1.5f * density))
    drawCircle(color, r * 0.5f, Offset(cx, cy), style = Stroke(width = 1f * density))
    drawCircle(color, r * 0.2f, Offset(cx, cy))
}
