package com.example.parkvahan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.parkvahan.navigation.ParkVahanNavGraph
import com.example.parkvahan.ui.theme.LocalParkVahanTheme
import com.example.parkvahan.ui.theme.ParkVahanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val isDark = androidx.compose.runtime.remember {
                androidx.compose.runtime.mutableStateOf(false)
            }

            ParkVahanTheme(darkTheme = isDark.value) {
                val t = LocalParkVahanTheme.current
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color    = t.bg
                ) {
                    val navController = rememberNavController()
                    ParkVahanNavGraph(navController = navController)
                }
            }
        }
    }
}