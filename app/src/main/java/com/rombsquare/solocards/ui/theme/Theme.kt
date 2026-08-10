package com.rombsquare.solocards.ui.theme

import android.app.Activity
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
    primary = primaryGreen,
    secondary = secondaryGreen,
    tertiary = tertiaryGreen,
    primaryContainer = primaryDarkGreen,
    secondaryContainer = secondaryDarkGreen,
    tertiaryContainer = tertiaryDarkGreen,
    surface = greenBackground,
    surfaceContainer = primaryGreen,
    surfaceContainerHigh = greenBackgroundHigh,
    surfaceVariant = greenBackground,
    background = greenBackground,
    outline = primaryGreen,
    outlineVariant = primaryGreen,

    onPrimary = primaryDarkGreen,
    onSecondary = secondaryDarkGreen,
    onTertiary = tertiaryDarkGreen,
    onPrimaryContainer = primaryGreen,
    onSecondaryContainer = secondaryGreen,
    onTertiaryContainer = tertiaryGreen,
    onSurface = greenishWhite,
    onBackground = greenishWhite,
    onSurfaceVariant = primaryGreen
)

private val LightColorScheme = lightColorScheme(
    primary = primaryDarkGreen,
    secondary = secondaryDarkGreen,
    tertiary = tertiaryDarkGreen

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun SolocardsTheme(
    darkTheme: Boolean = true, //isSystemInDarkTheme(),
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