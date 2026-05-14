package dev.danya.museum.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.danya.museum.feature.artworks.ui.nav.ArtworkDetailRoute
import dev.danya.museum.feature.artworks.ui.nav.ExhibitDetailRoute
import dev.danya.museum.feature.artworks.ui.nav.FavoritesRoute
import dev.danya.museum.feature.artworks.ui.nav.FeedRoute
import dev.danya.museum.feature.artworks.ui.nav.artworkDetailGraph
import dev.danya.museum.feature.artworks.ui.nav.favoritesGraph
import dev.danya.museum.feature.artworks.ui.nav.feedGraph
import dev.danya.museum.feature.homescreen.ui.nav.HomeRoute
import dev.danya.museum.feature.homescreen.ui.nav.homeScreenGraph
import dev.danya.museum.feature.search.ui.nav.SearchRoute
import dev.danya.museum.feature.search.ui.nav.searchGraph
import org.jetbrains.compose.resources.painterResource

@Composable
fun RootNavHost(
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                TopLevelDestination.entries.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.hasRoute(destination.route::class)
                    } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(destination.icon),
                                contentDescription = destination.label,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            homeScreenGraph(
                onNavigateToArtworks = {
                    navController.navigate(FeedRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToFavorites = {
                    navController.navigate(FavoritesRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToFeed = {
                    navController.navigate(FeedRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToSearch = {
                    navController.navigate(SearchRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
            searchGraph(
                onNavigateToDetail = { artworkId ->
                    navController.navigate(ArtworkDetailRoute(artworkId))
                },
            )
            feedGraph(
                onNavigateToDetail = { artworkId ->
                    navController.navigate(ArtworkDetailRoute(artworkId))
                },
            )
            artworkDetailGraph(
                onBack = { navController.popBackStack() },
            )
            favoritesGraph(
                onNavigateToDetail = { artworkId ->
                    navController.navigate(ArtworkDetailRoute(artworkId))
                },
                onNavigateToExhibitDetail = { exhibitId, exhibitName ->
                    navController.navigate(ExhibitDetailRoute(exhibitId, exhibitName))
                },
                onBack = { navController.popBackStack() },
            )
        }
    }
}