package com.rombsquare.solocards.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Peach colors (experiment)
//private val DarkColorScheme = darkColorScheme(
//    primary = Color(0xffffb5a0),
//    secondary = Color(0xffe7bdb2),
//    tertiary = Color(0xffd8c58d),
//    error = Color(0xffffb4ab),
//    primaryContainer = Color(0xff723523),
//    secondaryContainer = Color(0xff5d4037),
//    tertiaryContainer = Color(0xff534619),
//    errorContainer = Color(0xff93000a),
//    surface = Color(0xff1a110f),
//    surfaceContainer = Color(0xffffb5a0),
//    background = Color(0xFF1A110F),
//
//    onPrimary = Color(0xff561f0f),
//    onSecondary = Color(0xff442a22),
//    onTertiary = Color(0xff3b2f05),
//    onError = Color(0xf690005),
//    onPrimaryContainer = Color(0xffffdbd1),
//    onSecondaryContainer = Color(0xffffdbd1),
//    onTertiaryContainer = Color(0xfff5e1a7),
//    onErrorContainer = Color(0xfffdad6) ,
//    onSurface = Color(0xFFF1DFDA),
//    onBackground = Color(0xFFF1DFDA),
//)

private val DarkColorScheme = darkColorScheme(
    primary = lightGreen,
    secondary = lightYellowGreen,
    tertiary = lightBlue,
    primaryContainer = darkGreen,
    secondaryContainer = darkYellowGreen,
    tertiaryContainer = darkBlue,
    surface = GreenishBlack,
    surfaceContainer = darkGreen,
    surfaceContainerHigh = veryDarkGreen,
    surfaceVariant = darkerGreen,
    background = GreenishBlack,
    outline = lightGreen,
    outlineVariant = lightGreen,

    onPrimary = darkGreen,
    onSecondary = darkYellowGreen,
    onTertiary = darkBlue,
    onPrimaryContainer = lightGreen,
    onSecondaryContainer = lightYellowGreen,
    onTertiaryContainer = lightBlue,
    onSurface = greenishWhite,
    onBackground = greenishWhite,
    onSurfaceVariant = lightGreen,
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF26854B),
    secondary = Color(0xFF516350),
    tertiary = Color(0xFF39656C),
    error = Color(0xFFBA1A1A),

    primaryContainer = Color(0xFFB9F0B8),
    secondaryContainer = Color(0xFFD4E8D0),
    tertiaryContainer = Color(0xFFBCEAF2),
    errorContainer = Color(0xFFFFDAD6),

    surface = Color(0xFFF7FBF2),
    surfaceDim = Color(0xFFD7DBD3),
    surfaceContainer = Color(0xFFB9F0B8),
    surfaceContainerHigh = Color(0xFFE9EEE7), // Dialog and drawer color
    surfaceVariant = Color(0xFFB4EEC6), // Card color
    background = Color(0xFFF7FBF2),
    outline = darkGreen,
    outlineVariant = darkGreen,

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onError = Color.White,

    onPrimaryContainer = Color(0xFF1F5027),
    onSecondaryContainer = Color(0xFF3A4B39),
    onTertiaryContainer = Color(0xFF1F4D54),
    onErrorContainer = Color(0xFF93000A),

    onSurface = GreenishBlack,
    onBackground = GreenishBlack,
    onSurfaceVariant = darkGreen

)

@Composable
fun SolocardsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}