package dev.danya.museum.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class MuseumExtendedColors(
    val favorite: Color,
    val onFavorite: Color,
    val favoriteContainer: Color,
    val onFavoriteContainer: Color,

    val exhibit: Color,
    val onExhibit: Color,
    val exhibitContainer: Color,
    val onExhibitContainer: Color,

    val tag: Color,
    val onTag: Color,
    val tagContainer: Color,
    val onTagContainer: Color,
)

internal val LightMuseumExtendedColors = MuseumExtendedColors(
    favorite = MuseumPalette.Terracotta,
    onFavorite = Color.White,
    favoriteContainer = Color(0xFFF5C7BC),
    onFavoriteContainer = Color(0xFF3A0F03),

    exhibit = MuseumPalette.Mauve,
    onExhibit = Color.White,
    exhibitContainer = Color(0xFFEDD8E5),
    onExhibitContainer = Color(0xFF4D2840),

    tag = MuseumPalette.Slate,
    onTag = Color.White,
    tagContainer = Color(0xFFD2DCE5),
    onTagContainer = Color(0xFF14242F),
)

internal val DarkMuseumExtendedColors = MuseumExtendedColors(
    favorite = Color(0xFFD26E50),
    onFavorite = Color(0xFF3A0F03),
    favoriteContainer = Color(0xFF6F2310),
    onFavoriteContainer = Color(0xFFFBD8CD),

    exhibit = Color(0xFFD0A5C2),
    onExhibit = Color(0xFF381D2D),
    exhibitContainer = Color(0xFF6E4F62),
    onExhibitContainer = Color(0xFFF0D5E5),

    tag = Color(0xFF8FA6B8),
    onTag = Color(0xFF14242F),
    tagContainer = Color(0xFF324B5E),
    onTagContainer = Color(0xFFD2DCE5),
)

internal val LocalMuseumExtendedColors = staticCompositionLocalOf<MuseumExtendedColors> {
    error("MuseumExtendedColors not provided — wrap UI in MuseumTheme")
}

val MaterialTheme.extendedColors: MuseumExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalMuseumExtendedColors.current
