package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.ExhibitWithPreviews
import dev.danya.museum.feature.artworks.domain.repository.ExhibitRepository
import kotlinx.coroutines.flow.Flow

class GetExhibitsWithPreviewsUseCase(private val repository: ExhibitRepository) {
    operator fun invoke(previewLimit: Int = 6): Flow<Result<List<ExhibitWithPreviews>>> =
        repository.getExhibitsWithPreviews(previewLimit)
}