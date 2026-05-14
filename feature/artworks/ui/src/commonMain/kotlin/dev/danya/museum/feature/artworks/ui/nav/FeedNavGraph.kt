package dev.danya.museum.feature.artworks.ui.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.danya.museum.feature.artworks.ui.detail.ArtworkDetailScreen
import dev.danya.museum.feature.artworks.ui.feed.SwipeFeedScreen

fun NavGraphBuilder.feedGraph(
    onNavigateToDetail: (Int) -> Unit,
) {
    composable<FeedRoute> { SwipeFeedScreen(onNavigateToDetail = onNavigateToDetail) }
}

fun NavGraphBuilder.artworkDetailGraph(
    onBack: () -> Unit,
) {
    composable<ArtworkDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<ArtworkDetailRoute>()
        ArtworkDetailScreen(
            artworkId = route.artworkId,
            onBack = onBack,
        )
    }
}
