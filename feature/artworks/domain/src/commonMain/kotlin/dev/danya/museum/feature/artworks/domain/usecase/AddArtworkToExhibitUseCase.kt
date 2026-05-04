package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.repository.ExhibitRepository

class AddArtworkToExhibitUseCase(private val repository: ExhibitRepository) {
    suspend operator fun invoke(exhibitId: Long, artworkId: Int): Result<Unit> =
        repository.addArtworkToExhibit(exhibitId, artworkId)
}
