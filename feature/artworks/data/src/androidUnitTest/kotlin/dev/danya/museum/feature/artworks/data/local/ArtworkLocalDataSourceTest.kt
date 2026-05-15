package dev.danya.museum.feature.artworks.data.local

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ArtworkLocalDataSourceTest {

    private lateinit var dataSource: ArtworkLocalDataSource
    private lateinit var exhibitDataSource: ExhibitLocalDataSource

    @BeforeTest
    fun setup() {
        val db = createTestDatabase()
        val dispatchers = testDispatchers()
        dataSource = ArtworkLocalDataSource(db, dispatchers)
        exhibitDataSource = ExhibitLocalDataSource(db, dispatchers)
    }

    private fun insertArtwork(id: Int = 1, title: String = "Test Artwork") {
        dataSource.upsert(
            id = id,
            title = title,
            primaryImage = "https://example.com/img.jpg",
            artistDisplayName = "Artist",
            objectDate = "2000",
            culture = "Culture",
            period = "Period",
            dynasty = null,
            medium = "Oil",
            dimensions = "10x10",
            department = "Paintings",
            classification = "Painting",
            repository = "Met",
            cachedAt = System.currentTimeMillis(),
        )
    }

    @Test
    fun upsert_thenGetById_returnsArtwork() {
        insertArtwork(id = 42, title = "Starry Night")
        val result = dataSource.getById(42)
        assertNotNull(result)
        assertEquals("Starry Night", result.title)
        assertEquals("https://example.com/img.jpg", result.primaryImage)
    }

    @Test
    fun getById_unknownId_returnsNull() {
        assertNull(dataSource.getById(999))
    }

    @Test
    fun upsert_updatesExistingMetadata() {
        insertArtwork(id = 1, title = "Original")
        dataSource.upsert(
            id = 1,
            title = "Updated",
            primaryImage = "new.jpg",
            artistDisplayName = null,
            objectDate = null,
            culture = null,
            period = null,
            dynasty = null,
            medium = null,
            dimensions = null,
            department = "Modern Art",
            classification = null,
            repository = null,
            cachedAt = System.currentTimeMillis(),
        )
        val result = dataSource.getById(1)
        assertNotNull(result)
        assertEquals("Updated", result.title)
        assertEquals("new.jpg", result.primaryImage)
        assertEquals("Modern Art", result.department)
    }

    @Test
    fun setFavorite_marksFavorite() {
        insertArtwork(id = 1)
        assertFalse(dataSource.isFavorite(1))
        dataSource.setFavorite(1, isFavorite = true, favoritedAt = 1000L)
        assertTrue(dataSource.isFavorite(1))
    }

    @Test
    fun setFavorite_unmarksFavorite() {
        insertArtwork(id = 1)
        dataSource.setFavorite(1, isFavorite = true, favoritedAt = 1000L)
        dataSource.setFavorite(1, isFavorite = false, favoritedAt = null)
        assertFalse(dataSource.isFavorite(1))
    }

    @Test
    fun isFavorite_unknownId_returnsFalse() {
        assertFalse(dataSource.isFavorite(999))
    }

    @Test
    fun deleteIfOrphan_deletesNonFavoriteWithNoExhibits() {
        insertArtwork(id = 1)
        dataSource.deleteIfOrphan(1)
        assertNull(dataSource.getById(1))
    }

    @Test
    fun deleteIfOrphan_keepsFavorite() {
        insertArtwork(id = 1)
        dataSource.setFavorite(1, isFavorite = true, favoritedAt = 1000L)
        dataSource.deleteIfOrphan(1)
        assertNotNull(dataSource.getById(1))
    }

    @Test
    fun deleteIfOrphan_keepsArtworkInExhibit() {
        insertArtwork(id = 1)
        exhibitDataSource.createExhibit("Test Exhibit", System.currentTimeMillis())
        val exhibits = exhibitDataSource.getExhibits()
        // Need to collect the flow, but for this test just add directly
        exhibitDataSource.addArtwork(1L, 1, System.currentTimeMillis())
        dataSource.deleteIfOrphan(1)
        assertNotNull(dataSource.getById(1))
    }

    @Test
    fun upsert_preservesFavoriteStatus() {
        insertArtwork(id = 1)
        dataSource.setFavorite(1, isFavorite = true, favoritedAt = 1000L)
        insertArtwork(id = 1, title = "Updated")
        assertTrue(dataSource.isFavorite(1))
    }
}
