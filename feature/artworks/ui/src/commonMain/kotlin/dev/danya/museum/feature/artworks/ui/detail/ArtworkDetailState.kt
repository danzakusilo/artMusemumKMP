package dev.danya.museum.feature.artworks.ui.detail

import dev.danya.museum.feature.artworks.domain.entity.Artwork
import dev.danya.museum.feature.artworks.domain.entity.Exhibit

sealed class ArtworkDetailState {
    data object Loading : ArtworkDetailState()
    data class Content(
        val artwork: Artwork,
        val isFavorite: Boolean,
        val exhibits: List<Exhibit> = emptyList(),
    ) : ArtworkDetailState()
    data class Error(val message: String) : ArtworkDetailState()
}
