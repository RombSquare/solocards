package com.rombsquare.solocards.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme
import com.rombsquare.solocards.features.onboarding_feature.ui.OnboardingScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    // Don't autoscale fonts
    override fun attachBaseContext(newBase: Context) {
        val configuration = Configuration(newBase.resources.configuration).apply {
            fontScale = 1.0f
        }
        val context = newBase.createConfigurationContext(configuration)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            val prefs = remember { context.getSharedPreferences("app_prefs", MODE_PRIVATE) }

            // Is app launched for the first time?
            LaunchedEffect(Unit) {
                if (prefs.getBoolean("is_first_launch", true)) {
                    prefs.edit { putBoolean("is_first_launch", false) }
                }
            }

            var showOnboardingScreen by remember {
                mutableStateOf(prefs.getBoolean("is_first_launch", true))
            }

            SolocardsTheme {

                // If app is launched for the first time, show onboarding screen
                if (showOnboardingScreen) {
                    OnboardingScreen(
                        onClose = { showOnboardingScreen = false }
                    )
                } else {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavHost()
                    }
                }
            }
        }
    }
}