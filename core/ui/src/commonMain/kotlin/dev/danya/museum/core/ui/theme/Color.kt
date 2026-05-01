package dev.danya.museum.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object MuseumPalette {
    val Navy = Color(0xFF2B3A67)
    val Slate = Color(0xFF496A81)
    val Teal = Color(0xFF66999B)
    val Terracotta = Color(0xFFA33B20)
    val Mauve = Color(0xFFB37BA4)
}

internal val MuseumLightColorScheme: ColorScheme = lightColorScheme(
    primary = MuseumPalette.Navy,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD7DDEC),
    onPrimaryContainer = Color(0xFF0A1330),
    inversePrimary = Color(0xFFB5C2EE),

    secondary = MuseumPalette.Teal,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1E5E6),
    onSecondaryContainer = Color(0xFF1F3839),

    tertiary = MuseumPalette.Mauve,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEDD8E5),
    onTertiaryContainer = Color(0xFF4D2840),

    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),

    background = Color(0xFFFAF9F8),
    onBackground = Color(0xFF1B1C1F),
    surface = Color(0xFFFAF9F8),
    onSurface = Color(0xFF1B1C1F),
    surfaceVariant = Color(0xFFE1E2E8),
    onSurfaceVariant = Color(0xFF45464F),
    surfaceTint = MuseumPalette.Navy,
    inverseSurface = Color(0xFF2F3036),
    inverseOnSurface = Color(0xFFF1F0F4),

    outline = Color(0xFF757680),
    outlineVariant = Color(0xFFC5C6CD),
    scrim = Color.Black,
)

internal val MuseumDarkColorScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFFB5C2EE),
    onPrimary = Color(0xFF0A1330),
    primaryContainer = MuseumPalette.Navy,
    onPrimaryContainer = Color(0xFFD7DDEC),
    inversePrimary = MuseumPalette.Navy,

    secondary = Color(0xFF87BABB),
    onSecondary = Color(0xFF1F3839),
    secondaryContainer = Color(0xFF2D5253),
    onSecondaryContainer = Color(0xFFC7E5E6),

    tertiary = Color(0xFFD0A5C2),
    onTertiary = Color(0xFF381D2D),
    tertiaryContainer = Color(0xFF6E4F62),
    onTertiaryContainer = Color(0xFFF0D5E5),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),

    background = Color(0xFF131316),
    onBackground = Color(0xFFE5E2E9),
    surface = Color(0xFF131316),
    onSurface = Color(0xFFE5E2E9),
    surfaceVariant = Color(0xFF45464F),
    onSurfaceVariant = Color(0xFFC5C6CD),
    surfaceTint = Color(0xFFB5C2EE),
    inverseSurface = Color(0xFFE5E2E9),
    inverseOnSurface = Color(0xFF2F3036),

    outline = Color(0xFF8F9099),
    outlineVariant = Color(0xFF45464F),
    scrim = Color.Black,
)
