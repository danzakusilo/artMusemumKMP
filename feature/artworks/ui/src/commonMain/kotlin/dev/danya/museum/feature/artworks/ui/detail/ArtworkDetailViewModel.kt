package dev.danya.museum.feature.artworks.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.Exhibit
import dev.danya.museum.feature.artworks.domain.usecase.AddArtworkToExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.CreateExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetArtworkDetailUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetExhibitIdsForArtworkUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetExhibitsUseCase
import dev.danya.museum.feature.artworks.domain.usecase.IsFavoriteUseCase
import dev.danya.museum.feature.artworks.domain.usecase.RemoveArtworkFromExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArtworkDetailViewModel(
    private val artworkId: Int,
    private val getArtworkDetail: GetArtworkDetailUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val getExhibits: GetExhibitsUseCase,
    private val addArtworkToExhibit: AddArtworkToExhibitUseCase,
    private val createExhibit: CreateExhibitUseCase,
    private val getExhibitIdsForArtwork: GetExhibitIdsForArtworkUseCase,
    private val removeArtworkFromExhibit: RemoveArtworkFromExhibitUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ArtworkDetailState>(ArtworkDetailState.Loading)
    val state: StateFlow<ArtworkDetailState> = _state.asStateFlow()

    private var isFavorite = false
    private var exhibits: List<Exhibit> = emptyList()
    private var artworkExhibitIds: Set<Long> = emptySet()

    init {
        load()
        observeExhibits()
        observeArtworkExhibits()
    }

    private fun load() {
        viewModelScope.launch {
            _state.value = ArtworkDetailState.Loading
            val favResult = isFavoriteUseCase(artworkId)
            if (favResult is Result.Success) {
                isFavorite = favResult.data
            }
            when (val result = getArtworkDetail(artworkId)) {
                is Result.Success -> _state.value = ArtworkDetailState.Content(
                    artwork = result.data,
                    isFavorite = isFavorite,
                    exhibits = exhibits,
                    artworkExhibitIds = artworkExhibitIds,
                )
                is Result.Error -> _state.value = ArtworkDetailState.Error(
                    result.error.toString(),
                )
            }
        }
    }

    private fun observeExhibits() {
        viewModelScope.launch {
            getExhibits().collect { result ->
                if (result is Result.Success) {
                    exhibits = result.data
                    val current = _state.value as? ArtworkDetailState.Content ?: return@collect
                    _state.value = current.copy(exhibits = exhibits)
                }
            }
        }
    }

    private fun observeArtworkExhibits() {
        viewModelScope.launch {
            getExhibitIdsForArtwork(artworkId).collect { result ->
                if (result is Result.Success) {
                    artworkExhibitIds = result.data
                    val current = _state.value as? ArtworkDetailState.Content ?: return@collect
                    _state.value = current.copy(artworkExhibitIds = artworkExhibitIds)
                }
            }
        }
    }

    fun onToggleExhibit(exhibitId: Long) {
        viewModelScope.launch {
            if (exhibitId in artworkExhibitIds) {
                removeArtworkFromExhibit(exhibitId, artworkId)
            } else {
                addArtworkToExhibit(exhibitId, artworkId)
            }
        }
    }

    fun onToggleFavorite() {
        viewModelScope.launch {
            when (toggleFavorite(artworkId)) {
                is Result.Success -> {
                    isFavorite = !isFavorite
                    val current = _state.value as? ArtworkDetailState.Content ?: return@launch
                    _state.value = current.copy(isFavorite = isFavorite)
                }
                is Result.Error -> {}
            }
        }
    }

    fun onAddToExhibit(exhibitId: Long) {
        viewModelScope.launch {
            addArtworkToExhibit(exhibitId, artworkId)
        }
    }

    fun onCreateExhibit(name: String) {
        viewModelScope.launch {
            val result = createExhibit(name)
            if (result is Result.Success) {
                addArtworkToExhibit(result.data.id, artworkId)
            }
        }
    }

    fun retry() {
        load()
    }
}