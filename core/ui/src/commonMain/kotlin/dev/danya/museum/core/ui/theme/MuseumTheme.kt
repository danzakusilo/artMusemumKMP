package dev.danya.museum.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MuseumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val extended = if (darkTheme) DarkMuseumExtendedColors else LightMuseumExtendedColors
    CompositionLocalProvider(LocalMuseumExtendedColors provides extended) {
        MaterialTheme(
            colorScheme = if (darkTheme) MuseumDarkColorScheme else MuseumLightColorScheme,
            typography = MuseumTypography,
            content = content,
        )
    }
}
