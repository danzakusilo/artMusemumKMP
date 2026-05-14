package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.repository.ExhibitRepository
import kotlinx.coroutines.flow.Flow

class GetExhibitIdsForArtworkUseCase(private val repository: ExhibitRepository) {
    operator fun invoke(artworkId: Int): Flow<Result<Set<Long>>> =
        repository.getExhibitIdsForArtwork(artworkId)
}