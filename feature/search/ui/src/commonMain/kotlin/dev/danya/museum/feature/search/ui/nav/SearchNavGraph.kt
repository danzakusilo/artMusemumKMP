package dev.danya.museum.feature.search.ui.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.danya.museum.feature.search.ui.SearchScreen

fun NavGraphBuilder.searchGraph(
    onNavigateToDetail: (Int) -> Unit,
) {
    composable<SearchRoute> {
        SearchScreen(onNavigateToDetail = onNavigateToDetail)
    }
}
