package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.ArtworkSummary
import dev.danya.museum.feature.artworks.domain.repository.ArtworkRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(private val repository: ArtworkRepository) {
    operator fun invoke(): Flow<Result<List<ArtworkSummary>>> = repository.getFavorites()
}
