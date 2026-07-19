package com.polylove.marble.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.polylove.marble.ui.screens.*
import com.polylove.marble.ui.components.CardDialogOverlay
import com.polylove.marble.ui.theme.ObsidianBlack

// --- MAIN NAVIGATOR ---
@Composable
fun MainGameNavigator(viewModel: GameViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        AnimatedContent(
            targetState = viewModel.currentScreen,
            transitionSpec = {
                fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
            },
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                is GameScreen.Setup -> SetupScreen(viewModel)
                is GameScreen.Board -> BoardScreen(viewModel)
                is GameScreen.Win -> WinScreen(viewModel)
                is GameScreen.Editor -> EditorScreen(viewModel)
            }
        }
        
        CardDialogOverlay(viewModel)
    }
}
