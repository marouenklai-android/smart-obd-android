package com.example.obdapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0E2A1F),
    secondary = Color(0xFF0E2A1F),
    tertiary = Color(0xFF0E2A1F)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0E2A1F),
    secondary = Color(0xFF0E2A1F),
    tertiary = Color(0xFF0E2A1F),





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
val AppBgDark = Color(0xFFF5F5F5)
    //Color(0xFF102B21)
    //Color(0xFFF2F2F2)
    //Color(0xFF102840)
    //Color(0xFF102B21)
val AppCardDark = Color(0xFFFFFFFF)
    //Color(0xFF102840)
//Color(0xFF14362B)

//val AccentGreen =Color(0xFF6CFFB2)
val AccentGreen =Color(0xFF007AFF)
    //Color(0xFF00E5FF)
    //Color( 0xFF2ECC71)
    //Color(0xFF3DFFB5)
val Bg = Color(0xFFF5F5F5)
    //Color(0xFF0B1412)
val Card = Color(0xFFFFFFFF)
    //Color(0xFF12201C)
val Speed = Color(0xFF00E5FF)
val Rpm = Color(0xFF3DFFB5)
val Warning = Color(0xFFFF4D4D)
val TextPrimary = Color(0xFF212121)
    //Color.White
val TextSecondary = Color(0xFF666666)
    //Color(0xFF9FBFB6)
    //Color(0xFF0F172A)
    //Color(0xFF00E5FF)
    //Color(0xFF6CFFB2)
val AccentBlue = Color(0xFF6CB6FF)
val DarkBlue = Color(0xFF0E2433)
val NeonCyan  =Color(0xFF00E5FF)
val DeepNavy  = Color(0xFF102840)
val CyanMain = Color(0xFF00E5FF)
val CyanSoft = Color(0xFF66F0FF)
val CyanMuted = Color(0xFF00B8CC)
//Recommended dark palette
val BackgroundDark = Color(0xFF0F172A)
val SurfaceDark = Color(0xFF1C2333)

val AccentGray = Color(0xFFF2F2F2)

val AccentPrimary = Color(0xFF00E5FF)
val AccentSecondary = Color(0xFFFFC857)
//val AccentGreen = Color(0xFF2ECC71)

//val TextPrimary = Color(0xFFF2F2F2)
//val TextSecondary = Color(0xFF9CA3AF)
// --- Colors / Theme ---
val BackgroundGradient = Brush.verticalGradient(
    listOf(Color(0xFF0B0B0B), Color(0xFF0F2A20))
)
val AccentYellow = Color(0xFFFFC857)
val StatCardBackground = Color.White
val LabelColor = Color.Gray
val ValueColor = Color.Black

val AccentGreenSoft = Color(0xFF6CFFB2).copy(alpha = 0.25f)

val TextPrimaryDark = Color(0xFFEAF5F0)
val TextSecondaryDark = Color(0xFF9FBFB3)

val DangerRed = Color(0xFFFF6B6B)
val CardSurface = Color(0xFF132D24) // lighter than background

val LightColors = lightColorScheme(
    primary = Color(0xFF2563EB), // modern blue
    surfaceContainer = Color(0xFFF1F5F9)
)
@Composable
fun ObdAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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