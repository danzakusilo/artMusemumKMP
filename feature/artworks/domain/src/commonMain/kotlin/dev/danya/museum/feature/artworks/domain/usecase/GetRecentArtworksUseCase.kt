package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.Artwork
import dev.danya.museum.feature.artworks.domain.repository.ArtworkRepository

class GetRecentArtworksUseCase(private val repository: ArtworkRepository) {
    suspend operator fun invoke(): Result<List<Artwork>> =
        repository.getRecentArtworks()
}