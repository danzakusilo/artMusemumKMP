package dev.danya.museum.feature.artworks.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.danya.museum.core.common.dispatchers.AppDispatchers
import dev.danya.museum.core.database.Artwork as ArtworkEntity
import dev.danya.museum.core.database.MuseumDatabase
import dev.danya.museum.feature.artworks.domain.entity.Exhibit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExhibitLocalDataSource(
    private val db: MuseumDatabase,
    private val dispatchers: AppDispatchers,
) {
    fun getExhibits(): Flow<List<Exhibit>> =
        db.exhibitQueries.selectAllWithCount()
            .asFlow()
            .mapToList(dispatchers.io)
            .map { rows ->
                rows.map { row ->
                    Exhibit(
                        id = row.id,
                        name = row.name,
                        createdAt = row.createdAt,
                        artworkCount = row.artworkCount.toInt(),
                    )
                }
            }

    fun getExhibitPreviewArtworks(exhibitId: Long, limit: Int): List<ArtworkEntity> =
        db.exhibitArtworkQueries
            .selectArtworksForExhibitLimited(exhibitId, limit.toLong())
            .executeAsList()

    fun getArtworksForExhibit(exhibitId: Long): Flow<List<ArtworkEntity>> =
        db.exhibitArtworkQueries
            .selectArtworksForExhibit(exhibitId)
            .asFlow()
            .mapToList(dispatchers.io)

    fun createExhibit(name: String, createdAt: Long): Exhibit {
        db.exhibitQueries.insert(name = name, createdAt = createdAt)
        val id = db.exhibitQueries.lastInsertRowId().executeAsOne()
        return Exhibit(id = id, name = name, createdAt = createdAt, artworkCount = 0)
    }

    fun deleteExhibit(id: Long) {
        db.exhibitQueries.deleteById(id)
    }

    fun renameExhibit(id: Long, name: String) {
        db.exhibitQueries.rename(name = name, id = id)
    }

    fun addArtwork(exhibitId: Long, artworkId: Int, addedAt: Long) {
        db.exhibitArtworkQueries.add(
            exhibitId = exhibitId,
            artworkId = artworkId.toLong(),
            addedAt = addedAt,
        )
    }

    fun removeArtwork(exhibitId: Long, artworkId: Int) {
        db.exhibitArtworkQueries.remove(
            exhibitId = exhibitId,
            artworkId = artworkId.toLong(),
        )
    }

    fun removeAllForArtwork(artworkId: Int) {
        db.exhibitArtworkQueries.removeAllForArtwork(artworkId.toLong())
    }

    fun getExhibitIdsForArtwork(artworkId: Int): Flow<Set<Long>> =
        db.exhibitArtworkQueries
            .exhibitsContainingArtwork(artworkId.toLong())
            .asFlow()
            .mapToList(dispatchers.io)
            .map { exhibits -> exhibits.map { it.id }.toSet() }
}
