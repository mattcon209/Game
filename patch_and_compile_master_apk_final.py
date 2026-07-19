import os
import subprocess

print("--- FINAL MASTER COMPILE started ---")

# 1. Normalize line endings first
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

# 2. Write EmberParticle.kt
ep_content = """package com.polylove.marble.ui

data class EmberParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var size: Float,
    var alpha: Float,
    var life: Float,
    var maxLife: Float
)
"""
ep_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/EmberParticle.kt"
os.makedirs(os.path.dirname(ep_path), exist_ok=True)
with open(ep_path, "w", encoding="utf-8") as f:
    f.write(ep_content)
print("Wrote EmberParticle.kt!")

# 3. Write ChainsOfDesireLogo.kt
logo_content = """package com.polylove.marble.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polylove.marble.ui.theme.BrassGold
import com.polylove.marble.ui.theme.LatexCrimson
import com.polylove.marble.ui.theme.LeatherDarkPurple
import com.polylove.marble.ui.theme.SeductiveViolet

@Composable
fun ChainsOfDesireLogo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gold-leaf shackle-shield locked with keyhole, wrapped in velvet crimson gothic bat wings
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(SeductiveViolet, Color.Black),
                        radius = 120f
                    )
                )
                .border(2.dp, BrassGold, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Drawing the beautiful gothic bat wings and lock
            androidx.compose.foundation.Canvas(modifier = Modifier.size(48.dp)) {
                val w = size.width
                val h = size.height
                
                // 1. Draw crimson bat wings
                val wingPath = Path().apply {
                    // Left wing
                    moveTo(w * 0.5f, h * 0.4f)
                    cubicTo(w * 0.3f, h * 0.1f, w * 0.1f, h * 0.2f, 0f, h * 0.4f)
                    cubicTo(w * 0.1f, h * 0.55f, w * 0.25f, h * 0.6f, w * 0.45f, h * 0.55f)
                    
                    // Right wing
                    moveTo(w * 0.5f, h * 0.4f)
                    cubicTo(w * 0.7f, h * 0.1f, w * 0.9f, h * 0.2f, w, h * 0.4f)
                    cubicTo(w * 0.9f, h * 0.55f, w * 0.75f, h * 0.6f, w * 0.55f, h * 0.55f)
                    close()
                }
                drawPath(wingPath, color = LatexCrimson.copy(alpha = 0.85f))
                
                // 2. Draw gold shackle-shield center
                drawCircle(
                    color = BrassGold,
                    radius = w * 0.22f,
                    center = center
                )
                drawCircle(
                    color = Color.Black,
                    radius = w * 0.18f,
                    center = center
                )
                
                // 3. Draw keyhole inside lock
                val keyholePath = Path().apply {
                    addOval(Rect(center = center, radius = w * 0.06f))
                    moveTo(w * 0.47f, h * 0.52f)
                    lineTo(w * 0.53f, h * 0.52f)
                    lineTo(w * 0.55f, h * 0.68f)
                    lineTo(w * 0.45f, h * 0.68f)
                    close()
                }
                drawPath(keyholePath, color = BrassGold)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Gothic Title Header
        Text(
            text = "CHAINS OF DESIRE",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BrassGold,
            fontFamily = FontFamily.Serif,
            letterSpacing = 1.5.sp,
            textAlign = TextAlign.Center
        )
        
        // Seductive subtitle
        Text(
            text = "An Intimate Poly-BDSM Summoning Ritual",
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = LatexCrimson,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
"""
logo_path = "PolyLoveMarble/app/src/main/java/com/polylove/marble/ui/ChainsOfDesireLogo.kt"
with open(logo_path, "w", encoding="utf-8") as f:
    f.write(logo_content)
print("Wrote ChainsOfDesireLogo.kt!")

print("--- RUNNING GRADLE DIRECT BUILD ---")
# Run assembly debug directly
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
    print("Master APK compiled successfully!")
else:
    print(f"FAILED DIRECT BUILD! Code: {result.returncode}")
    exit(1)
