package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.AppError
import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.repository.ExhibitRepository

class RenameExhibitUseCase(private val repository: ExhibitRepository) {
    suspend operator fun invoke(id: Long, name: String): Result<Unit> {
        if (name.isBlank()) return Result.Error(
            AppError.UnknownError(IllegalArgumentException("Exhibit name must not be blank"))
        )
        return repository.renameExhibit(id, name.trim())
    }
}