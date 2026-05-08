package dev.danya.museum.feature.artworks.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.usecase.GetArtworkDetailUseCase
import dev.danya.museum.feature.artworks.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArtworkDetailViewModel(
    private val artworkId: Int,
    private val getArtworkDetail: GetArtworkDetailUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ArtworkDetailState>(ArtworkDetailState.Loading)
    val state: StateFlow<ArtworkDetailState> = _state.asStateFlow()

    private var isFavorite = false

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _state.value = ArtworkDetailState.Loading
            when (val result = getArtworkDetail(artworkId)) {
                is Result.Success -> _state.value = ArtworkDetailState.Content(
                    artwork = result.data,
                    isFavorite = isFavorite,
                )
                is Result.Error -> _state.value = ArtworkDetailState.Error(
                    result.error.toString(),
                )
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

    fun retry() {
        load()
    }
}
