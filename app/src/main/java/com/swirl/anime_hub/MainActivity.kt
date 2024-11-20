package com.swirl.anime_hub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.swirl.anime_hub.ui.navigation.AnimeNavGraph
import com.swirl.anime_hub.ui.theme.AnimeHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            AnimeHubTheme {
                val navController = rememberNavController()
                AnimeNavGraph(navController)
            }
        }
    }
}