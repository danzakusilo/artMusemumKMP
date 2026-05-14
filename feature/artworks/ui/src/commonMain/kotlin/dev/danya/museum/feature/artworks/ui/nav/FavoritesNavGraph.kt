package dev.danya.museum.feature.artworks.ui.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.danya.museum.feature.artworks.ui.favorites.FavoritesScreen

fun NavGraphBuilder.favoritesGraph(
    onNavigateToDetail: (Int) -> Unit,
) {
    composable<FavoritesRoute> {
        FavoritesScreen(onNavigateToDetail = onNavigateToDetail)
    }
}