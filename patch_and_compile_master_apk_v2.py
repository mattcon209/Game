import os
import subprocess

print("--- master compile v2 started ---")

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

# 1. Re-write CommonUI.kt with explicit androidx.compose.foundation.Canvas and density definitions
cui_content = """package com.polylove.marble.ui.components

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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

// A premium kinky play card with thick gold-leaf borders and glowing runic panels
@Composable
fun KinkyCard(
    modifier: Modifier = Modifier,
    borderColor: Color = SeductiveViolet.copy(alpha = 0.4f),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LeatherDarkPurple.copy(alpha = 0.92f), Color(0xFF0D0814).copy(alpha = 0.92f))
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(borderColor, BrassGold.copy(alpha = 0.6f), borderColor)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
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

    androidx.compose.foundation.Canvas(
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
    val density = LocalDensity.current.density
    val infiniteTransition = rememberInfiniteTransition(label = "PortalPulse")
    val portalPulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF280E1F), Color(0xFF0B050D))
                )
            )
            .border(2.dp, BrassGold, RoundedCornerShape(14.dp))
            .then(if (enabled) Modifier.clickable { onClick() } else Modifier)
            .shadow(if (enabled) 12.dp else 0.dp, RoundedCornerShape(14.dp), ambientColor = BrassGold)
    ) {
        // Draw the glowing celestial orb background inside the portal button! (Matches sketch-up button dome!)
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            
            // Draw central glowing cosmic sphere
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(LatexCrimson.copy(alpha = 0.8f * portalPulse), Color.Transparent),
                    center = Offset(w / 2f, h / 2f),
                    radius = w * 0.22f
                ),
                radius = w * 0.22f,
                center = Offset(w / 2f, h / 2f)
            )
            
            // Draw elegant gold framing accents along left and right arches
            val thickness = 2f * density
            drawPath(
                path = Path().apply {
                    moveTo(12f * density, h)
                    cubicTo(w * 0.15f, h * 0.2f, w * 0.35f, h * 0.2f, w * 0.45f, h * 0.35f)
                    
                    moveTo(w - (12f * density), h)
                    cubicTo(w * 0.85f, h * 0.2f, w * 0.65f, h * 0.2f, w * 0.55f, h * 0.35f)
                },
                color = BrassGold.copy(alpha = 0.4f),
                style = Stroke(width = thickness)
            )
        }

        Text(
            text = text.uppercase(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = textColor.copy(alpha = alpha),
            modifier = Modifier.align(Alignment.Center),
            letterSpacing = 2.sp,
            fontFamily = FontFamily.Serif
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
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
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
    val density = LocalDensity.current.density
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (checked) SeductiveViolet.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onCheckedChange(!checked) }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            drawPadlockVector(isClosed = !checked, color = if (checked) BrassGold else SteelGrey)
        }
    }
}
"""

with open(cui_path, "w", encoding="utf-8") as f:
    f.write(cui_content)
print("Wrote and finalized CommonUI.kt!")

# 2. Patch GrimoireOfSafety.kt
gos_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/GrimoireOfSafety.kt"
if os.path.exists(gos_path):
    with open(gos_path, "r", encoding="utf-8") as f:
        gos = f.read()
    if "import androidx.compose.ui.text.font.FontWeight" not in gos:
        gos = gos.replace("import androidx.compose.ui.Modifier", "import androidx.compose.ui.Modifier\nimport androidx.compose.ui.text.font.FontWeight")
        with open(gos_path, "w", encoding="utf-8") as f:
            f.write(gos)
        print("Patched GrimoireOfSafety.kt!")

# 3. Patch SetupScreen.kt (Make sure the avatar call parameter is correct)
setup_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/SetupScreen.kt"
with open(setup_path, "r", encoding="utf-8") as f:
    setup_content = f.read()

setup_content = setup_content.replace(
    "OccultPlayerAvatar(\n                                        playerColor = player.color,\n                                        playerName = player.name,\n                                        playerIndex = index,",
    "OccultPlayerAvatar(\n                                        playerColor = player.color,\n                                        playerIndex = index,"
)
with open(setup_path, "w", encoding="utf-8") as f:
    f.write(setup_content)
print("SetupScreen avatar parameters corrected!")

# 4. Patch ScoreboardPanel.kt
sb_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/components/ScoreboardPanel.kt"
with open(sb_path, "r", encoding="utf-8") as f:
    sb_content = f.read()
sb_content = sb_content.replace(
    "OccultPlayerAvatar(\n                            playerColor = p.color,\n                            playerName = p.name,\n                            playerIndex = if (pIndex != -1) pIndex else 0,",
    "OccultPlayerAvatar(\n                            playerColor = p.color,\n                            playerIndex = if (pIndex != -1) pIndex else 0,"
)
with open(sb_path, "w", encoding="utf-8") as f:
    f.write(sb_content)
print("ScoreboardPanel avatar parameters corrected!")

# 5. Patch WinScreen.kt (Fix border unresolved reference)
win_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/screens/WinScreen.kt"
with open(win_path, "r", encoding="utf-8") as f:
    win_content = f.read()
if "import androidx.compose.foundation.border" not in win_content:
    win_content = win_content.replace(
        "import androidx.compose.foundation.background",
        "import androidx.compose.foundation.background\nimport androidx.compose.foundation.border"
    )
    with open(win_path, "w", encoding="utf-8") as f:
        f.write(win_content)
    print("WinScreen imports corrected!")

print("--- RUNNING DIRECT GRADLE BUILD ---")
result = subprocess.run(
    ["/home/user/gradle-bin/gradle-8.5/bin/gradle", "assembleDebug", "--no-daemon"],
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
    print("Master APK built successfully!")
else:
    print(f"FAILED MASTER COMPILE! Code: {result.returncode}")
    exit(1)
