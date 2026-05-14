package dev.danya.museum.feature.artworks.ui.exhibitions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.usecase.GetExhibitArtworksUseCase
import dev.danya.museum.feature.artworks.domain.usecase.RenameExhibitUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExhibitDetailViewModel(
    private val exhibitId: Long,
    private val getExhibitArtworks: GetExhibitArtworksUseCase,
    private val renameExhibit: RenameExhibitUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ExhibitDetailState>(ExhibitDetailState.Loading)
    val state: StateFlow<ExhibitDetailState> = _state.asStateFlow()

    init {
        observeArtworks()
    }

    private fun observeArtworks() {
        viewModelScope.launch {
            getExhibitArtworks(exhibitId).collect { result ->
                _state.value = when (result) {
                    is Result.Success -> {
                        if (result.data.isEmpty()) ExhibitDetailState.Empty
                        else ExhibitDetailState.Content(result.data)
                    }
                    is Result.Error -> ExhibitDetailState.Error(result.error.toString())
                }
            }
        }
    }

    fun onRename(name: String) {
        viewModelScope.launch {
            renameExhibit(exhibitId, name)
        }
    }
}