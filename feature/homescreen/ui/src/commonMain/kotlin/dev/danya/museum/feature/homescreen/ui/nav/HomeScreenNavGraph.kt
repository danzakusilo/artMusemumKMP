package dev.danya.museum.feature.homescreen.ui.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.danya.museum.feature.homescreen.ui.HomeScreen

fun NavGraphBuilder.homeScreenGraph(
    onNavigateToArtworks: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToFeed: () -> Unit,
) {
    composable<HomeRoute> {
        HomeScreen(
            onNavigateToArtworks = onNavigateToArtworks,
            onNavigateToFavorites = onNavigateToFavorites,
            onNavigateToFeed = onNavigateToFeed,
        )
    }
}
