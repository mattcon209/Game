package com.polylove.marble.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.R
import com.polylove.marble.ui.theme.*

@Composable
fun GemstoneButton(gemstoneColor: GemstoneColor, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(32.dp).clip(CircleShape).background(gemstoneColor.color)
        .border(width = if (isSelected) 2.5.dp else 1.dp, color = if (isSelected) GoldPrimary else Color.White.copy(alpha = 0.25f), shape = CircleShape)
        .clickable { onClick() })
}

@Composable
fun SeductiveLeatherBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxSize().background(ObsidianBlack)) {
        content()
    }
}

@Composable
fun GlassPanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
        .background(DarkCardBg.copy(alpha = 0.85f))
        .border(1.dp, CrimsonPrimary.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
        .shadow(8.dp, RoundedCornerShape(8.dp), ambientColor = CrimsonPrimary).padding(20.dp)
    ) {
        content()
    }
}

@Composable
fun OrnateGothicDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier = modifier.padding(vertical = 8.dp), thickness = 1.dp, color = GoldPrimary.copy(alpha = 0.5f))
}

@Composable
fun OccultPlayerAvatar(playerColor: Color, playerIndex: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(44.dp).clip(CircleShape)
        .background(Brush.radialGradient(colors = listOf(Color.White.copy(alpha = 0.7f), playerColor, Color.Black.copy(alpha = 0.85f)), center = Offset(24f, 24f), radius = 50f))
        .border(2.dp, GoldPrimary, CircleShape).shadow(6.dp, CircleShape))
}

@Composable
fun KinkyBoltToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Switch(checked = checked, onCheckedChange = onCheckedChange, modifier = modifier,
        colors = SwitchDefaults.colors(checkedThumbColor = CyanGlow, checkedTrackColor = CyanGlow.copy(alpha = 0.3f), uncheckedThumbColor = CrimsonGlow, uncheckedTrackColor = CrimsonDeep))
}

@Composable
fun KinkyPadlockCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Checkbox(checked = checked, onCheckedChange = onCheckedChange, modifier = modifier,
        colors = CheckboxDefaults.colors(checkedColor = GoldPrimary, uncheckedColor = TextMuted, checkmarkColor = DarkSurface))
}

@Composable
fun GothicPremiumCard(modifier: Modifier = Modifier, borderColor: Color = GoldPrimary.copy(alpha = 0.4f), content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
        .background(DarkCardBg).border(1.dp, CrimsonPrimary.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
        .shadow(8.dp, RoundedCornerShape(8.dp), ambientColor = CrimsonPrimary).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        content()
    }
}

@Composable
fun PremiumGothicButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, isPurple: Boolean = false, enabled: Boolean = true) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "ButtonScale")
    val bgColor = if (isPurple) PurpleArcane else CrimsonGlow
    Box(modifier = modifier.graphicsLayer(scaleX = scale, scaleY = scale).fillMaxWidth().height(52.dp)
        .clip(RoundedCornerShape(26.dp)).background(bgColor).border(1.5.dp, GoldPrimary, RoundedCornerShape(26.dp))
        .shadow(12.dp, RoundedCornerShape(26.dp), ambientColor = CrimsonGlow)
        .clickable(interactionSource = interactionSource, indication = null, enabled = enabled) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress); onClick()
        }, contentAlignment = Alignment.Center) {
        Text(text = text, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 2.sp, color = Color.White)
    }
}

// KinkyCard component used by multiple files
@Composable
fun KinkyCard(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Transparent,
    isShort: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = if (isShort) 150.dp else 240.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        val bgRes = if (isShort) R.drawable.gothic_frame_short else R.drawable.gothic_frame_large
        val bitmap = ImageBitmap.imageResource(id = bgRes)
        
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val imgW = bitmap.width.toFloat()
            val imgH = bitmap.height.toFloat()
            val srcTopH = 115f
            val srcBotH = 88f
            val srcMidH = imgH - srcTopH - srcBotH
            val dstTopH = srcTopH * (w / imgW)
            val dstBotH = srcBotH * (w / imgW)
            val dstMidH = h - dstTopH - dstBotH
            
            drawImage(image = bitmap, srcOffset = IntOffset(0, 0), srcSize = IntSize(bitmap.width, srcTopH.toInt()), dstOffset = IntOffset(0, 0), dstSize = IntSize(w.toInt(), dstTopH.toInt()))
            if (dstMidH > 0) {
                drawImage(image = bitmap, srcOffset = IntOffset(0, srcTopH.toInt()), srcSize = IntSize(bitmap.width, srcMidH.toInt()), dstOffset = IntOffset(0, dstTopH.toInt()), dstSize = IntSize(w.toInt(), dstMidH.toInt()))
            }
            drawImage(image = bitmap, srcOffset = IntOffset(0, (imgH - srcBotH).toInt()), srcSize = IntSize(bitmap.width, srcBotH.toInt()), dstOffset = IntOffset(0, (h - dstBotH).toInt()), dstSize = IntSize(w.toInt(), dstBotH.toInt()))
        }
        
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 48.dp, end = 48.dp, top = 54.dp, bottom = 42.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}
