package com.srg.neighbourhoodwatchcompanion.presenter.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = DarkCharcoal,
    onPrimary = White,
    secondary = LightGrayishBlue,
    onSecondary = DarkCharcoal,
    background = VeryLightGray,
    onBackground = DarkCharcoal,
    surface = White,
    onSurface = DarkCharcoal,
    onSurfaceVariant = DarkGray
)

private val DarkColors = darkColorScheme(
    primary = White,
    onPrimary = DarkCharcoal,
    secondary = LightGray,
    onSecondary = White,
    background = DarkCharcoal,
    onBackground = White,
    surface = DarkCharcoal,
    onSurface = White
)


@Composable
fun NeighbourhoodWatchCompanionTheme(
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

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
