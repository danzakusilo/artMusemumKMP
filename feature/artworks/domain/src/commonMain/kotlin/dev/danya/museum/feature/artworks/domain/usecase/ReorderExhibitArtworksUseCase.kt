package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.repository.ExhibitRepository

class ReorderExhibitArtworksUseCase(private val repository: ExhibitRepository) {
    suspend operator fun invoke(exhibitId: Long, orderedArtworkIds: List<Int>): Result<Unit> =
        repository.reorderExhibitArtworks(exhibitId, orderedArtworkIds)
}
