package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.repository.ExhibitRepository

class DeleteExhibitUseCase(private val repository: ExhibitRepository) {
    suspend operator fun invoke(id: Long): Result<Unit> =
        repository.deleteExhibit(id)
}