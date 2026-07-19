package com.polylove.marble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.polylove.marble.ui.GameViewModel
import com.polylove.marble.ui.MainGameNavigator
import com.polylove.marble.ui.theme.PolyLoveMarbleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize the game state model
        val gameViewModel = GameViewModel()
        
        setContent {
            PolyLoveMarbleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainGameNavigator(gameViewModel)
                }
            }
        }
    }
}
