package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = IslamicGreenDark,
    secondary = DarkPrimary,
    tertiary = AmberGold,
    background = DarkBg,
    surface = DarkSurface,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    surfaceVariant = Color(0xFF1B2E24),
    onSurfaceVariant = DarkTextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = IslamicGreen,
    secondary = DeepForest,
    tertiary = AmberGold,
    background = LightBg,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary,
    surfaceVariant = MintGreenLight,
    onSurfaceVariant = LightTextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
