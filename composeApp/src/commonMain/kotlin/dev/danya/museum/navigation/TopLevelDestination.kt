package dev.danya.museum.navigation

import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.ic_nav_discover
import demo.composeapp.generated.resources.ic_nav_favorites
import demo.composeapp.generated.resources.ic_nav_home
import demo.composeapp.generated.resources.ic_nav_search
import dev.danya.museum.feature.artworks.ui.nav.FeedRoute
import dev.danya.museum.feature.homescreen.ui.nav.HomeRoute
import dev.danya.museum.feature.search.ui.nav.SearchRoute
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource

@Serializable data object FavoritesRoute

enum class TopLevelDestination(
    val route: Any,
    val label: String,
    val icon: DrawableResource,
) {
    HOME(HomeRoute, "Home", Res.drawable.ic_nav_home),
    SEARCH(SearchRoute, "Search", Res.drawable.ic_nav_search),
    DISCOVER(FeedRoute, "Discover", Res.drawable.ic_nav_discover),
    FAVORITES(FavoritesRoute, "Favorites", Res.drawable.ic_nav_favorites),
}