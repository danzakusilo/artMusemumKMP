package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.ArtworkSummary
import dev.danya.museum.feature.artworks.domain.repository.ExhibitRepository
import kotlinx.coroutines.flow.Flow

class GetExhibitArtworksUseCase(private val repository: ExhibitRepository) {
    operator fun invoke(exhibitId: Long): Flow<Result<List<ArtworkSummary>>> =
        repository.getExhibitArtworks(exhibitId)
}