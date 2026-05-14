package dev.danya.museum.feature.artworks.ui.exhibitions

import dev.danya.museum.feature.artworks.domain.entity.ExhibitWithPreviews

sealed class ExhibitionsState {
    data object Loading : ExhibitionsState()
    data object Empty : ExhibitionsState()
    data class Content(val exhibits: List<ExhibitWithPreviews>) : ExhibitionsState()
    data class Error(val message: String) : ExhibitionsState()
}