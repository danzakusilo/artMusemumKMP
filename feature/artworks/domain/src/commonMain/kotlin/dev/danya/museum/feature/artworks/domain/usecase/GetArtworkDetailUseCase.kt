package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.Artwork
import dev.danya.museum.feature.artworks.domain.repository.ArtworkRepository

class GetArtworkDetailUseCase(private val repository: ArtworkRepository) {
    suspend operator fun invoke(id: Int): Result<Artwork> = repository.getArtworkDetail(id)
}
