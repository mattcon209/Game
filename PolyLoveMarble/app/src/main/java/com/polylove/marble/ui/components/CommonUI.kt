package com.polylove.marble.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
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

// Gemstone button drawing with a solid flat look to match chosen player colors inside selection lists
@Composable
fun GemstoneButton(
    gemstoneColor: GemstoneColor,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(gemstoneColor.color)
            .border(
                width = if (isSelected) 2.5.dp else 1.dp,
                color = if (isSelected) BrassGold else Color.White.copy(alpha = 0.25f),
                shape = CircleShape
            )
            .clickable { onClick() }
    )
}

// SEDUCTIVE BACKGROUND: Uses the actual, photorealistic sketched JPEG as the background image!
// This makes the game look exactly like your gorgeous candle-lit gothic coven layout!
@Composable
fun SeductiveLeatherBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.seductive_bg),
            contentDescription = "Occult Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        content()
    }
}

// A premium kinky play card styled with your actual, photorealistic gothic metallic scroll frames!
// Stretches to wrap any content inside, hiding the gray background corners perfectly!
@Composable
fun KinkyCard(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Transparent, // Backward compatible!
    isShort: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = if (isShort) 150.dp else 240.dp) // Set perfect default minimum height!
            .clip(RoundedCornerShape(16.dp))
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Load the actual photorealistic gothic frame image as the background!
        val bgRes = if (isShort) R.drawable.gothic_frame_short else R.drawable.gothic_frame_large
        val bitmap = ImageBitmap.imageResource(id = bgRes)
        
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            
            val imgW = bitmap.width.toFloat()
            val imgH = bitmap.height.toFloat()
            
            // Slice definitions: top border (115px), bottom border (88px), middle stretches
            val srcTopH = 115f
            val srcBotH = 88f
            val srcMidH = imgH - srcTopH - srcBotH
            
            // Calculate destination heights while keeping top/bottom aspect ratio correct
            val dstTopH = srcTopH * (w / imgW)
            val dstBotH = srcBotH * (w / imgW)
            val dstMidH = h - dstTopH - dstBotH
            
            // 1. Draw top border slice
            drawImage(
                image = bitmap,
                srcOffset = IntOffset(0, 0),
                srcSize = IntSize(bitmap.width, srcTopH.toInt()),
                dstOffset = IntOffset(0, 0),
                dstSize = IntSize(w.toInt(), dstTopH.toInt())
            )
            
            // 2. Draw middle stretchable parchment
            if (dstMidH > 0) {
                drawImage(
                    image = bitmap,
                    srcOffset = IntOffset(0, srcTopH.toInt()),
                    srcSize = IntSize(bitmap.width, srcMidH.toInt()),
                    dstOffset = IntOffset(0, dstTopH.toInt()),
                    dstSize = IntSize(w.toInt(), dstMidH.toInt())
                )
            }
            
            // 3. Draw bottom border slice
            drawImage(
                image = bitmap,
                srcOffset = IntOffset(0, (imgH - srcBotH).toInt()),
                srcSize = IntSize(bitmap.width, srcBotH.toInt()),
                dstOffset = IntOffset(0, (h - dstBotH).toInt()),
                dstSize = IntSize(w.toInt(), dstBotH.toInt())
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 48.dp, top = 54.dp, bottom = 42.dp), // Extra padding to stay inside the gold scroll frame!
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

// Ornate gothic metal divider with center glowing star separator
@Composable
fun OrnateGothicDivider(modifier: Modifier = Modifier) {
    val density = LocalDensity.current.density
    val infiniteTransition = rememberInfiniteTransition(label = "DividerSparkle")
    val sparkleScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "SparkleScale"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {
        val w = size.width
        val h = size.height
        val cy = h / 2f
        val cx = w / 2f
        
        // Draw dual gold horizontal rods
        val goldBrush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, BrassGold.copy(alpha = 0.7f), Color.Transparent),
            startX = 0f,
            endX = w
        )
        drawLine(
            brush = goldBrush,
            start = Offset(0f, cy - 1f * density),
            end = Offset(w, cy - 1f * density),
            strokeWidth = 1.5f * density
        )
        drawLine(
            brush = goldBrush,
            start = Offset(0f, cy + 1f * density),
            end = Offset(w, cy + 1f * density),
            strokeWidth = 1.5f * density
        )
        
        // Draw decorative center diamond filigree
        drawPath(
            path = Path().apply {
                moveTo(cx, cy - 6f * density)
                lineTo(cx + 10f * density, cy)
                lineTo(cx, cy + 6f * density)
                lineTo(cx - 10f * density, cy)
                close()
            },
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF3A1C2C), Color(0xFF14070F)),
                center = Offset(cx, cy)
            )
        )
        drawPath(
            path = Path().apply {
                moveTo(cx, cy - 6f * density)
                lineTo(cx + 10f * density, cy)
                lineTo(cx, cy + 6f * density)
                lineTo(cx - 10f * density, cy)
                close()
            },
            color = BrassGold,
            style = Stroke(width = 1f * density)
        )
        
        // Draw center glowing gemstone star
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFF007F), Color.Transparent),
                center = Offset(cx, cy),
                radius = 8f * density * sparkleScale
            ),
            radius = 8f * density * sparkleScale,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = Color.White,
            radius = 2.5f * density * sparkleScale,
            center = Offset(cx, cy)
        )
    }
}

// Occult Player Avatar: Simple, solid, clean flat color circles inside beveled gold rings!
// Reverted completely back to flat shackle-color gemstone look, as requested!
@Composable
fun OccultPlayerAvatar(
    playerColor: Color,
    playerIndex: Int,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.density
    
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(playerColor)
            .border(2.dp, BrassGold, CircleShape)
            .shadow(4.dp, CircleShape)
    )
}

// Gothic Celestial Archway Portal button wrapper for BEGIN SESSION
@Composable
fun LeatherStrapButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = LatexCrimson,
    textColor: Color = Color.White
) {
    val alpha = if (enabled) 1f else 0.5f
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(29.dp)) // Pill-shape clipping to hide outer background!
            .border(2.dp, BrassGold, RoundedCornerShape(29.dp))
            .shadow(12.dp, RoundedCornerShape(29.dp), ambientColor = BrassGold)
            .then(if (enabled) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.begin_session_btn),
            contentDescription = text,
            contentScale = ContentScale.Crop, // Crops and fits perfectly
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Sphere 3D radial-gradient gemstone sliders on leather strap tracks!
@Composable
fun KinkyBoltToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbOffset by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "ToggleOffset"
    )
    
    Box(
        modifier = modifier
            .width(56.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF09040C))
            .border(1.5.dp, BrassGold, RoundedCornerShape(14.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(2.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val radius = h * 0.44f
            val startX = radius + 2f
            val endX = w - radius - 2f
            val currentX = startX + (endX - startX) * thumbOffset
            
            // Draw dark leather strap track slot
            drawRoundRect(
                color = Color(0xFF221319),
                topLeft = Offset(startX - 2f, h/2 - 4f),
                size = Size(endX - startX + 4f, 8f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            
            // Draw 3D Radial-Gradient Gemstone sphere!
            // Checked = Sapphire Blue Sphere; Unchecked = Ruby Red/Heart Sphere
            val sphereCenter = Offset(currentX, h / 2f)
            val gemstoneColors = if (checked) {
                listOf(Color(0xFF00BFFF), Color(0xFF00008B), Color.Black)
            } else {
                listOf(Color(0xFFFF007F), Color(0xFF8B0000), Color.Black)
            }
            
            drawCircle(
                brush = Brush.radialGradient(
                    colors = gemstoneColors,
                    center = Offset(currentX - radius / 3f, h / 2f - radius / 3f),
                    radius = radius * 1.1f
                ),
                radius = radius,
                center = sphereCenter
            )
            
            // Highlight shine for 3D glass effect
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.7f), Color.Transparent),
                    center = Offset(currentX - radius / 3f, h / 2f - radius / 3f),
                    radius = radius * 0.5f
                ),
                radius = radius * 0.5f,
                center = Offset(currentX - radius / 4f, h / 2f - radius / 4f)
            )
        }
    }
}

// --- 🔏 CUSTOM PADLOCK-THEMED EXPANSION CHECKBOX ---
@Composable
fun KinkyPadlockCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (checked) SeductiveViolet.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onCheckedChange(!checked) }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPadlockVector(isClosed = !checked, color = if (checked) BrassGold else SteelGrey)
        }
    }
}
