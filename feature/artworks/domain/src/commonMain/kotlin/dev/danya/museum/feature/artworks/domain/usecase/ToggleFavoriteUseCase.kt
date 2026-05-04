package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.repository.ArtworkRepository

class ToggleFavoriteUseCase(private val repository: ArtworkRepository) {
    suspend operator fun invoke(artworkId: Int): Result<Unit> = repository.toggleFavorite(artworkId)
}
