package dev.danya.museum.feature.artworks.ui.exhibitions

import dev.danya.museum.feature.artworks.domain.entity.ArtworkSummary

sealed class ExhibitDetailState {
    data object Loading : ExhibitDetailState()
    data object Empty : ExhibitDetailState()
    data class Content(val artworks: List<ArtworkSummary>) : ExhibitDetailState()
    data class Error(val message: String) : ExhibitDetailState()
}