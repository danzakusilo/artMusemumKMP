package dev.danya.museum.feature.artworks.domain.repository

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.Exhibit
import kotlinx.coroutines.flow.Flow

interface ExhibitRepository {
    fun getExhibits(): Flow<Result<List<Exhibit>>>
    suspend fun createExhibit(name: String): Result<Exhibit>
    suspend fun addArtworkToExhibit(exhibitId: Long, artworkId: Int): Result<Unit>
    suspend fun removeArtworkFromExhibit(exhibitId: Long, artworkId: Int): Result<Unit>
}
