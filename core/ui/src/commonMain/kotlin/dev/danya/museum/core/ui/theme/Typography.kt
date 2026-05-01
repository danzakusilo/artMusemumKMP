package dev.danya.museum.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Base = Typography()

internal val MuseumTypography: Typography = Base.copy(
    displayLarge = Base.displayLarge.copy(
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.5).sp,
    ),
    displayMedium = Base.displayMedium.copy(fontWeight = FontWeight.SemiBold),
    displaySmall = Base.displaySmall.copy(fontWeight = FontWeight.SemiBold),

    headlineLarge = Base.headlineLarge.copy(
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.25).sp,
    ),
    headlineMedium = Base.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
    headlineSmall = Base.headlineSmall.copy(fontWeight = FontWeight.SemiBold),

    titleLarge = Base.titleLarge.copy(fontWeight = FontWeight.SemiBold),
    titleMedium = Base.titleMedium.copy(fontWeight = FontWeight.Medium),

    labelLarge = Base.labelLarge.copy(fontWeight = FontWeight.Medium),
)
