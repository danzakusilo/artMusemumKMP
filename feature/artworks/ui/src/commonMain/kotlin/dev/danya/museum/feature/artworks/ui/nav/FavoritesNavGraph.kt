package dev.danya.museum.feature.artworks.ui.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.danya.museum.feature.artworks.ui.exhibitions.ExhibitDetailScreen
import dev.danya.museum.feature.artworks.ui.favorites.FavoritesScreen

fun NavGraphBuilder.favoritesGraph(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToExhibitDetail: (Long, String) -> Unit,
    onBack: () -> Unit,
) {
    composable<FavoritesRoute> {
        FavoritesScreen(
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToExhibitDetail = onNavigateToExhibitDetail,
        )
    }
    composable<ExhibitDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<ExhibitDetailRoute>()
        ExhibitDetailScreen(
            exhibitId = route.exhibitId,
            exhibitName = route.exhibitName,
            onBack = onBack,
            onNavigateToDetail = onNavigateToDetail,
        )
    }
}