package dev.danya.museum.feature.artworks.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.danya.museum.core.database.MuseumDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExhibitLocalDataSourceTest {

    private lateinit var db: MuseumDatabase
    private lateinit var exhibitDs: ExhibitLocalDataSource
    private lateinit var artworkDs: ArtworkLocalDataSource

    @BeforeTest
    fun setup() {
        db = createTestDatabase()
        val dispatchers = testDispatchers()
        exhibitDs = ExhibitLocalDataSource(db, dispatchers)
        artworkDs = ArtworkLocalDataSource(db, dispatchers)
    }

    private fun insertArtwork(id: Int) {
        artworkDs.upsert(
            id = id,
            title = "Artwork $id",
            primaryImage = "https://example.com/$id.jpg",
            artistDisplayName = "Artist",
            objectDate = "2000",
            culture = null,
            period = null,
            dynasty = null,
            medium = null,
            dimensions = null,
            department = "Dept",
            classification = null,
            repository = null,
            cachedAt = System.currentTimeMillis(),
        )
    }

    @Test
    fun createExhibit_returnsExhibitWithId() {
        val exhibit = exhibitDs.createExhibit("My Exhibition", 1000L)
        assertEquals("My Exhibition", exhibit.name)
        assertEquals(1000L, exhibit.createdAt)
        assertEquals(0, exhibit.artworkCount)
        assertTrue(exhibit.id > 0)
    }

    @Test
    fun getExhibits_returnsCreatedExhibits() = runTest {
        exhibitDs.createExhibit("First", 1000L)
        exhibitDs.createExhibit("Second", 2000L)
        val exhibits = exhibitDs.getExhibits().first()
        assertEquals(2, exhibits.size)
        assertEquals("Second", exhibits[0].name)
        assertEquals("First", exhibits[1].name)
    }

    @Test
    fun renameExhibit_updatesName() = runTest {
        val exhibit = exhibitDs.createExhibit("Old Name", 1000L)
        exhibitDs.renameExhibit(exhibit.id, "New Name")
        val exhibits = exhibitDs.getExhibits().first()
        assertEquals("New Name", exhibits[0].name)
    }

    @Test
    fun deleteExhibit_removesFromList() = runTest {
        val exhibit = exhibitDs.createExhibit("To Delete", 1000L)
        exhibitDs.deleteExhibit(exhibit.id)
        val exhibits = exhibitDs.getExhibits().first()
        assertTrue(exhibits.isEmpty())
    }

    @Test
    fun addArtwork_appearsInExhibit() = runTest {
        insertArtwork(1)
        val exhibit = exhibitDs.createExhibit("Exhibit", 1000L)
        exhibitDs.addArtwork(exhibit.id, 1, System.currentTimeMillis())
        val artworks = exhibitDs.getArtworksForExhibit(exhibit.id).first()
        assertEquals(1, artworks.size)
        assertEquals(1L, artworks[0].id)
    }

    @Test
    fun addArtwork_updatesExhibitCount() = runTest {
        insertArtwork(1)
        insertArtwork(2)
        val exhibit = exhibitDs.createExhibit("Exhibit", 1000L)
        exhibitDs.addArtwork(exhibit.id, 1, 1000L)
        exhibitDs.addArtwork(exhibit.id, 2, 2000L)
        val exhibits = exhibitDs.getExhibits().first()
        assertEquals(2, exhibits[0].artworkCount)
    }

    @Test
    fun removeArtwork_removesFromExhibit() = runTest {
        insertArtwork(1)
        val exhibit = exhibitDs.createExhibit("Exhibit", 1000L)
        exhibitDs.addArtwork(exhibit.id, 1, 1000L)
        exhibitDs.removeArtwork(exhibit.id, 1)
        val artworks = exhibitDs.getArtworksForExhibit(exhibit.id).first()
        assertTrue(artworks.isEmpty())
    }

    @Test
    fun removeAllForArtwork_removesFromAllExhibits() = runTest {
        insertArtwork(1)
        val ex1 = exhibitDs.createExhibit("Ex1", 1000L)
        val ex2 = exhibitDs.createExhibit("Ex2", 2000L)
        exhibitDs.addArtwork(ex1.id, 1, 1000L)
        exhibitDs.addArtwork(ex2.id, 1, 2000L)
        exhibitDs.removeAllForArtwork(1)
        val a1 = exhibitDs.getArtworksForExhibit(ex1.id).first()
        val a2 = exhibitDs.getArtworksForExhibit(ex2.id).first()
        assertTrue(a1.isEmpty())
        assertTrue(a2.isEmpty())
    }

    @Test
    fun reorderArtworks_changesSortOrder() = runTest {
        insertArtwork(1)
        insertArtwork(2)
        insertArtwork(3)
        val exhibit = exhibitDs.createExhibit("Exhibit", 1000L)
        exhibitDs.addArtwork(exhibit.id, 1, 1000L)
        exhibitDs.addArtwork(exhibit.id, 2, 2000L)
        exhibitDs.addArtwork(exhibit.id, 3, 3000L)
        exhibitDs.reorderArtworks(exhibit.id, listOf(3, 1, 2))
        val artworks = exhibitDs.getArtworksForExhibit(exhibit.id).first()
        assertEquals(listOf(3L, 1L, 2L), artworks.map { it.id })
    }

    @Test
    fun deleteExhibit_cascadesJoinTable() = runTest {
        insertArtwork(1)
        val exhibit = exhibitDs.createExhibit("Exhibit", 1000L)
        exhibitDs.addArtwork(exhibit.id, 1, 1000L)
        exhibitDs.deleteExhibit(exhibit.id)
        val count = db.exhibitArtworkQueries.countForExhibit(exhibit.id).executeAsOne()
        assertEquals(0L, count)
    }

    @Test
    fun getExhibitPreviewArtworks_respectsLimit() {
        insertArtwork(1)
        insertArtwork(2)
        insertArtwork(3)
        val exhibit = exhibitDs.createExhibit("Exhibit", 1000L)
        exhibitDs.addArtwork(exhibit.id, 1, 1000L)
        exhibitDs.addArtwork(exhibit.id, 2, 2000L)
        exhibitDs.addArtwork(exhibit.id, 3, 3000L)
        val previews = exhibitDs.getExhibitPreviewArtworks(exhibit.id, limit = 2)
        assertEquals(2, previews.size)
    }

    @Test
    fun getExhibitIdsForArtwork_returnsContainingExhibits() = runTest {
        insertArtwork(1)
        val ex1 = exhibitDs.createExhibit("Ex1", 1000L)
        val ex2 = exhibitDs.createExhibit("Ex2", 2000L)
        exhibitDs.createExhibit("Ex3", 3000L)
        exhibitDs.addArtwork(ex1.id, 1, 1000L)
        exhibitDs.addArtwork(ex2.id, 1, 2000L)
        val ids = exhibitDs.getExhibitIdsForArtwork(1).first()
        assertEquals(setOf(ex1.id, ex2.id), ids)
    }

    @Test
    fun addArtwork_duplicateIsIgnored() = runTest {
        insertArtwork(1)
        val exhibit = exhibitDs.createExhibit("Exhibit", 1000L)
        exhibitDs.addArtwork(exhibit.id, 1, 1000L)
        exhibitDs.addArtwork(exhibit.id, 1, 2000L)
        val artworks = exhibitDs.getArtworksForExhibit(exhibit.id).first()
        assertEquals(1, artworks.size)
    }
}
