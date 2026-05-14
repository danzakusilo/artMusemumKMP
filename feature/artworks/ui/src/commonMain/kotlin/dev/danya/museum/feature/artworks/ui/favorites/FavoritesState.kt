package dev.danya.museum.feature.artworks.ui.favorites

import dev.danya.museum.feature.artworks.domain.entity.ArtworkSummary

enum class SortOrder { DATE_SAVED, TITLE, DEPARTMENT }

sealed class FavoritesState {
    data object Loading : FavoritesState()
    data object Empty : FavoritesState()
    data class Content(
        val artworks: List<ArtworkSummary>,
        val sortOrder: SortOrder,
        val departmentFilter: String?,
        val availableDepartments: List<String>,
        val selectedIds: Set<Int>,
    ) : FavoritesState()
    data class Error(val message: String) : FavoritesState()
}