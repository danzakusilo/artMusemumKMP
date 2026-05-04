package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.repository.ExhibitRepository

class RemoveArtworkFromExhibitUseCase(private val repository: ExhibitRepository) {
    suspend operator fun invoke(exhibitId: Long, artworkId: Int): Result<Unit> =
        repository.removeArtworkFromExhibit(exhibitId, artworkId)
}
