package com.polylove.marble.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.polylove.marble.ui.theme.GoldPrimary
import com.polylove.marble.ui.theme.CrimsonGlow
import com.polylove.marble.ui.theme.DarkCardBg

@Composable
fun ChainsOfDesireLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(48.dp)) {
        drawCircle(color = GoldPrimary, radius = size.width / 2)
        drawCircle(color = CrimsonGlow, radius = size.width / 3)
    }
}
