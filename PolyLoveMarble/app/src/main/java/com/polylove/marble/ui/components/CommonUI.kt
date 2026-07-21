package com.polylove.marble.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.ui.theme.*

@Composable
fun GemstoneButton(gemstoneColor: GemstoneColor, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(32.dp).clip(CircleShape).background(gemstoneColor.color)
        .border(width = if (isSelected) 2.5.dp else 1.dp, color = if (isSelected) GoldPrimary else Color.White.copy(alpha = 0.25f), shape = CircleShape)
        .clickable { onClick() })
}

@Composable
fun SeductiveLeatherBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color = ObsidianBlack)
            drawRect(brush = Brush.radialGradient(colors = listOf(CrimsonPrimary.copy(alpha = 0.25f), Color.Transparent), center = Offset(size.width * 0.5f, 0f), radius = size.height * 0.7f))
            drawRect(brush = Brush.radialGradient(colors = listOf(Color(0xFF2B103C).copy(alpha = 0.4f), Color.Transparent), center = Offset(size.width * 0.8f, size.height * 0.8f), radius = size.height * 0.6f))
        }
        content()
    }
}

@Composable
fun GlassPanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Box(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
        .background(DarkCardBg.copy(alpha = 0.85f))
        .border(1.dp, CrimsonPrimary.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
        .shadow(8.dp, RoundedCornerShape(8.dp), ambientColor = CrimsonPrimary).padding(20.dp)) {
        Column(modifier = Modifier.fillMaxWidth(), content = content)
    }
}

@Composable
fun OrnateGothicDivider(modifier: Modifier = Modifier) {
    val density = LocalDensity.current.density
    val infiniteTransition = rememberInfiniteTransition(label = "DividerSparkle")
    val sparkleScale by infiniteTransition.animateFloat(initialValue = 0.8f, targetValue = 1.2f, animationSpec = infiniteRepeatable(animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Reverse), label = "SparkleScale")
    Canvas(modifier = modifier.fillMaxWidth().height(20.dp)) {
        val w = size.width; val h = size.height; val cy = h / 2f; val cx = w / 2f
        val goldBrush = Brush.horizontalGradient(colors = listOf(Color.Transparent, GoldPrimary.copy(alpha = 0.7f), Color.Transparent), startX = 0f, endX = w)
        drawLine(brush = goldBrush, start = Offset(0f, cy - 1f * density), end = Offset(w, cy - 1f * density), strokeWidth = 1.5f * density)
        drawLine(brush = goldBrush, start = Offset(0f, cy + 1f * density), end = Offset(w, cy + 1f * density), strokeWidth = 1.5f * density)
        drawPath(path = Path().apply { moveTo(cx, cy - 6f * density); lineTo(cx + 10f * density, cy); lineTo(cx, cy + 6f * density); lineTo(cx - 10f * density, cy); close() }, color = GoldPrimary, style = Stroke(width = 1f * density))
        drawCircle(brush = Brush.radialGradient(colors = listOf(CrimsonGlow, Color.Transparent), center = Offset(cx, cy), radius = 8f * density * sparkleScale), radius = 8f * density * sparkleScale, center = Offset(cx, cy))
        drawCircle(color = Color.White, radius = 2.5f * density * sparkleScale, center = Offset(cx, cy))
    }
}

@Composable
fun OccultPlayerAvatar(playerColor: Color, playerIndex: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(44.dp).clip(CircleShape)
        .background(Brush.radialGradient(colors = listOf(Color.White.copy(alpha = 0.7f), playerColor, Color.Black.copy(alpha = 0.85f)), center = Offset(24f, 24f), radius = 50f))
        .border(2.dp, GoldPrimary, CircleShape).shadow(6.dp, CircleShape))
}

@Composable
fun KinkyBoltToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val thumbOffset by animateFloatAsState(targetValue = if (checked) 1f else 0f, animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing), label = "ToggleOffset")
    Box(modifier = modifier.width(56.dp).height(28.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFF09040C))
        .border(1.5.dp, GoldPrimary, RoundedCornerShape(14.dp)).clickable { onCheckedChange(!checked) }.padding(2.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width; val h = size.height; val radius = h * 0.44f
            val startX = radius + 2f; val endX = w - radius - 2f; val currentX = startX + (endX - startX) * thumbOffset
            drawRect(color = Color(0xFF221319), topLeft = Offset(startX - 2f, h/2 - 4f), size = androidx.compose.ui.geometry.Size(endX - startX + 4f, 8f))
            val gemstoneColors = if (checked) listOf(CyanGlow, Color(0xFF00008B), Color.Black) else listOf(CrimsonGlow, Color(0xFF8B0000), Color.Black)
            drawCircle(brush = Brush.radialGradient(colors = gemstoneColors, center = Offset(currentX - radius / 3f, h / 2f - radius / 3f), radius = radius * 1.1f), radius = radius, center = Offset(currentX, h / 2f))
        }
    }
}

@Composable
fun KinkyPadlockCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(36.dp).clip(RoundedCornerShape(8.dp))
        .background(if (checked) PurpleArcane.copy(alpha = 0.2f) else Color.Transparent)
        .clickable { onCheckedChange(!checked) }.padding(4.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) { drawPadlockVector(isClosed = !checked, color = if (checked) GoldPrimary else TextMuted) }
    }
}

@Composable
fun GothicPremiumCard(modifier: Modifier = Modifier, borderColor: Color = GoldPrimary.copy(alpha = 0.4f), content: @Composable ColumnScope.() -> Unit) {
    Box(modifier = modifier.fillMaxWidth().defaultMinSize(minHeight = 150.dp).clip(RoundedCornerShape(8.dp))
        .background(DarkCardBg).border(1.dp, CrimsonPrimary.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
        .shadow(8.dp, RoundedCornerShape(8.dp), ambientColor = CrimsonPrimary).padding(16.dp)) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) { content() }
    }
}

@Composable
fun PremiumGothicButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, isPurple: Boolean = false, enabled: Boolean = true) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "ButtonScale")
    val bgGradient = if (isPurple) Brush.verticalGradient(colors = listOf(PurpleArcane.copy(alpha = 0.8f), Color(0xFF2D0A3e)))
    else Brush.verticalGradient(colors = listOf(CrimsonGlow.copy(alpha = 0.8f), CrimsonDeep))
    Box(modifier = modifier.graphicsLayer(scaleX = scale, scaleY = scale).fillMaxWidth().height(52.dp)
        .clip(RoundedCornerShape(26.dp)).background(bgGradient).border(1.5.dp, GoldPrimary, RoundedCornerShape(26.dp))
        .shadow(12.dp, RoundedCornerShape(26.dp), ambientColor = CrimsonGlow)
        .clickable(interactionSource = interactionSource, indication = null, enabled = enabled) { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onClick() }, contentAlignment = Alignment.Center) {
        Text(text = text, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 2.sp, color = Color.White)
    }
}
