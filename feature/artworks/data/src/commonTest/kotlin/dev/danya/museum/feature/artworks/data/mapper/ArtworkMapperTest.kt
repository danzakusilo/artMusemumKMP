package dev.danya.museum.feature.artworks.data.mapper

import dev.danya.museum.feature.artworks.data.remote.dto.ArtworkDetailDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ArtworkMapperTest {

    private val fullDto = ArtworkDetailDto(
        objectID = 42,
        title = "Starry Night",
        primaryImage = "https://example.com/large.jpg",
        primaryImageSmall = "https://example.com/small.jpg",
        artistDisplayName = "Vincent van Gogh",
        objectDate = "1889",
        culture = "Dutch",
        period = "Post-Impressionism",
        dynasty = null,
        medium = "Oil on canvas",
        dimensions = "73.7 x 92.1 cm",
        department = "European Paintings",
        classification = "Paintings",
        repository = "Metropolitan Museum of Art",
    )

    @Test
    fun dtoToDomain_mapsAllFields() {
        val result = fullDto.toDomain()
        assertEquals(42, result.id)
        assertEquals("Starry Night", result.title)
        assertEquals("https://example.com/small.jpg", result.primaryImageUrl)
        assertEquals("Vincent van Gogh", result.artistName)
        assertEquals("1889", result.objectDate)
        assertEquals("Dutch", result.culture)
        assertEquals("Post-Impressionism", result.period)
        assertNull(result.dynasty)
        assertEquals("Oil on canvas", result.medium)
        assertEquals("73.7 x 92.1 cm", result.dimensions)
        assertEquals("European Paintings", result.department)
        assertEquals("Paintings", result.classification)
        assertEquals("Metropolitan Museum of Art", result.repository)
    }

    @Test
    fun dtoToDomain_prefersSmallImage() {
        val result = fullDto.toDomain()
        assertEquals("https://example.com/small.jpg", result.primaryImageUrl)
    }

    @Test
    fun dtoToDomain_fallsBackToLargeImage() {
        val dto = fullDto.copy(primaryImageSmall = null)
        assertEquals("https://example.com/large.jpg", dto.toDomain().primaryImageUrl)
    }

    @Test
    fun dtoToDomain_blankSmallImageFallsBackToLarge() {
        val dto = fullDto.copy(primaryImageSmall = "  ")
        assertEquals("https://example.com/large.jpg", dto.toDomain().primaryImageUrl)
    }

    @Test
    fun dtoToDomain_blankFieldsBecomeNull() {
        val dto = fullDto.copy(
            artistDisplayName = "",
            objectDate = "  ",
            culture = "",
            medium = "",
            classification = "  ",
        )
        val result = dto.toDomain()
        assertNull(result.artistName)
        assertNull(result.objectDate)
        assertNull(result.culture)
        assertNull(result.medium)
        assertNull(result.classification)
    }

    @Test
    fun dtoToDomain_bothImagesNullGivesNull() {
        val dto = fullDto.copy(primaryImage = null, primaryImageSmall = null)
        assertNull(dto.toDomain().primaryImageUrl)
    }

    @Test
    fun dtoToSummary_mapsFields() {
        val result = fullDto.toSummary()
        assertEquals(42, result.id)
        assertEquals("Starry Night", result.title)
        assertEquals("https://example.com/small.jpg", result.primaryImageUrl)
        assertEquals("Vincent van Gogh", result.artistName)
        assertEquals("1889", result.objectDate)
        assertEquals("European Paintings", result.department)
    }

    @Test
    fun dtoToSummary_blankArtistBecomesNull() {
        val dto = fullDto.copy(artistDisplayName = "  ")
        assertNull(dto.toSummary().artistName)
    }
}
