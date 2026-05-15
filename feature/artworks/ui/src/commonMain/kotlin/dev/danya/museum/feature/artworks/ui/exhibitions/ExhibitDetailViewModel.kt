package dev.danya.museum.feature.artworks.ui.exhibitions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.ArtworkSummary
import dev.danya.museum.feature.artworks.domain.usecase.GetExhibitArtworksUseCase
import dev.danya.museum.feature.artworks.domain.usecase.RenameExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.ReorderExhibitArtworksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExhibitDetailViewModel(
    private val exhibitId: Long,
    private val getExhibitArtworks: GetExhibitArtworksUseCase,
    private val renameExhibit: RenameExhibitUseCase,
    private val reorderExhibitArtworks: ReorderExhibitArtworksUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ExhibitDetailState>(ExhibitDetailState.Loading)
    val state: StateFlow<ExhibitDetailState> = _state.asStateFlow()

    private var localOrder: MutableList<ArtworkSummary>? = null

    init {
        observeArtworks()
    }

    private fun observeArtworks() {
        viewModelScope.launch {
            getExhibitArtworks(exhibitId).collect { result ->
                if (localOrder != null) return@collect
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

    fun onMove(fromIndex: Int, toIndex: Int) {
        val current = (_state.value as? ExhibitDetailState.Content) ?: return
        val reordered = localOrder ?: current.artworks.toMutableList()
        reordered.add(toIndex, reordered.removeAt(fromIndex))
        localOrder = reordered
        _state.value = current.copy(artworks = reordered.toList())
    }

    fun onDragEnd() {
        val order = localOrder ?: return
        localOrder = null
        viewModelScope.launch {
            reorderExhibitArtworks(exhibitId, order.map { it.id })
        }
    }

    fun onRename(name: String) {
        viewModelScope.launch {
            renameExhibit(exhibitId, name)
        }
    }
}