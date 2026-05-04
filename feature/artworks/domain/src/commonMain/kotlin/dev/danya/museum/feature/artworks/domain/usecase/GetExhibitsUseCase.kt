package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.Exhibit
import dev.danya.museum.feature.artworks.domain.repository.ExhibitRepository
import kotlinx.coroutines.flow.Flow

class GetExhibitsUseCase(private val repository: ExhibitRepository) {
    operator fun invoke(): Flow<Result<List<Exhibit>>> = repository.getExhibits()
}
