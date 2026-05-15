package dev.danya.museum.feature.artworks.domain.usecase

import dev.danya.museum.core.common.result.AppError
import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.Artwork
import dev.danya.museum.feature.artworks.domain.entity.ArtworkSummary
import dev.danya.museum.feature.artworks.domain.repository.ArtworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchArtworksUseCaseTest {

    @Test
    fun blankQueryReturnsEmptySuccess() = runTest {
        val useCase = SearchArtworksUseCase(FakeArtworkRepository())
        val result = useCase("   ")
        assertTrue(result is Result.Success)
        assertTrue(result.data.isEmpty())
    }

    @Test
    fun emptyQueryReturnsEmptySuccess() = runTest {
        val useCase = SearchArtworksUseCase(FakeArtworkRepository())
        val result = useCase("")
        assertTrue(result is Result.Success)
        assertTrue(result.data.isEmpty())
    }

    @Test
    fun trimsQueryBeforeDelegating() = runTest {
        val repo = FakeArtworkRepository()
        val useCase = SearchArtworksUseCase(repo)
        useCase("  Rembrandt  ")
        assertEquals("Rembrandt", repo.lastSearchQuery)
    }

    @Test
    fun passesDepartmentIdToRepository() = runTest {
        val repo = FakeArtworkRepository()
        val useCase = SearchArtworksUseCase(repo)
        useCase("art", departmentId = 11)
        assertEquals(11, repo.lastDepartmentId)
    }

    @Test
    fun passesNullDepartmentByDefault() = runTest {
        val repo = FakeArtworkRepository()
        val useCase = SearchArtworksUseCase(repo)
        useCase("art")
        assertEquals(null, repo.lastDepartmentId)
    }

    @Test
    fun passesArtistOrCultureFlag() = runTest {
        val repo = FakeArtworkRepository()
        val useCase = SearchArtworksUseCase(repo)
        useCase("van Gogh", artistOrCulture = true)
        assertEquals(true, repo.lastArtistOrCulture)
    }

    @Test
    fun passesHasImagesFlag() = runTest {
        val repo = FakeArtworkRepository()
        val useCase = SearchArtworksUseCase(repo)
        useCase("sculpture", hasImages = false)
        assertEquals(false, repo.lastHasImages)
    }

    @Test
    fun returnsRepositoryResults() = runTest {
        val summary = ArtworkSummary(1, "Test", null, null, null, "Dept")
        val repo = FakeArtworkRepository(searchResults = listOf(summary))
        val useCase = SearchArtworksUseCase(repo)
        val result = useCase("test")
        assertTrue(result is Result.Success)
        assertEquals(1, result.data.size)
        assertEquals("Test", result.data[0].title)
    }

    @Test
    fun repositoryErrorPropagates() = runTest {
        val repo = FakeArtworkRepository(shouldFail = true)
        val useCase = SearchArtworksUseCase(repo)
        val result = useCase("test")
        assertTrue(result is Result.Error)
    }

    @Test
    fun blankQueryDoesNotCallRepository() = runTest {
        val repo = FakeArtworkRepository()
        val useCase = SearchArtworksUseCase(repo)
        useCase("")
        assertEquals(0, repo.searchCallCount)
    }

    private class FakeArtworkRepository(
        private val searchResults: List<ArtworkSummary> = emptyList(),
        private val shouldFail: Boolean = false,
    ) : ArtworkRepository {
        var lastSearchQuery: String? = null
        var lastDepartmentId: Int? = null
        var lastArtistOrCulture: Boolean? = null
        var lastHasImages: Boolean? = null
        var searchCallCount = 0

        override suspend fun searchArtworks(
            query: String,
            departmentId: Int?,
            artistOrCulture: Boolean,
            hasImages: Boolean,
        ): Result<List<ArtworkSummary>> {
            searchCallCount++
            lastSearchQuery = query
            lastDepartmentId = departmentId
            lastArtistOrCulture = artistOrCulture
            lastHasImages = hasImages
            return if (shouldFail) Result.Error(AppError.NetworkError(500, "fail"))
            else Result.Success(searchResults)
        }

        override suspend fun loadMoreSearchResults(): Result<List<ArtworkSummary>> =
            Result.Success(emptyList())
        override suspend fun getArtworkDetail(id: Int): Result<Artwork> = throw NotImplementedError()
        override suspend fun getRecentArtworks(): Result<List<Artwork>> = throw NotImplementedError()
        override suspend fun getArtworkFeedPage(departmentId: Int, limit: Int): Result<List<Artwork>> = throw NotImplementedError()
        override fun getFavorites(): Flow<Result<List<ArtworkSummary>>> = emptyFlow()
        override suspend fun isFavorite(artworkId: Int): Result<Boolean> = throw NotImplementedError()
        override suspend fun toggleFavorite(artworkId: Int): Result<Unit> = throw NotImplementedError()
    }
}
