package dev.danya.museum.feature.artworks.domain.repository

import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.ArtworkSummary
import dev.danya.museum.feature.artworks.domain.entity.Exhibit
import dev.danya.museum.feature.artworks.domain.entity.ExhibitWithPreviews
import kotlinx.coroutines.flow.Flow

interface ExhibitRepository {
    fun getExhibits(): Flow<Result<List<Exhibit>>>
    fun getExhibitsWithPreviews(previewLimit: Int): Flow<Result<List<ExhibitWithPreviews>>>
    fun getExhibitArtworks(exhibitId: Long): Flow<Result<List<ArtworkSummary>>>
    suspend fun createExhibit(name: String): Result<Exhibit>
    suspend fun deleteExhibit(id: Long): Result<Unit>
    suspend fun renameExhibit(id: Long, name: String): Result<Unit>
    suspend fun addArtworkToExhibit(exhibitId: Long, artworkId: Int): Result<Unit>
    suspend fun removeArtworkFromExhibit(exhibitId: Long, artworkId: Int): Result<Unit>
}
