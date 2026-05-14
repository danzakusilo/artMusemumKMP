package dev.danya.museum.feature.artworks.ui.exhibitions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.usecase.CreateExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.DeleteExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetExhibitsWithPreviewsUseCase
import dev.danya.museum.feature.artworks.domain.usecase.RenameExhibitUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExhibitionsViewModel(
    private val getExhibitsWithPreviews: GetExhibitsWithPreviewsUseCase,
    private val deleteExhibit: DeleteExhibitUseCase,
    private val renameExhibit: RenameExhibitUseCase,
    private val createExhibit: CreateExhibitUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ExhibitionsState>(ExhibitionsState.Loading)
    val state: StateFlow<ExhibitionsState> = _state.asStateFlow()

    init {
        observeExhibits()
    }

    private fun observeExhibits() {
        viewModelScope.launch {
            getExhibitsWithPreviews().collect { result ->
                _state.value = when (result) {
                    is Result.Success -> {
                        if (result.data.isEmpty()) ExhibitionsState.Empty
                        else ExhibitionsState.Content(result.data)
                    }
                    is Result.Error -> ExhibitionsState.Error(result.error.toString())
                }
            }
        }
    }

    fun onDeleteExhibit(id: Long) {
        viewModelScope.launch {
            deleteExhibit(id)
        }
    }

    fun onRenameExhibit(id: Long, name: String) {
        viewModelScope.launch {
            renameExhibit(id, name)
        }
    }

    fun onCreateExhibit(name: String) {
        viewModelScope.launch {
            createExhibit(name)
        }
    }
}