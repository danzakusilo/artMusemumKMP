package dev.danya.museum.feature.homescreen.ui.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.danya.museum.feature.homescreen.ui.HomeScreen

fun NavGraphBuilder.homeScreenGraph() {
    composable<HomeRoute> { HomeScreen() }
}