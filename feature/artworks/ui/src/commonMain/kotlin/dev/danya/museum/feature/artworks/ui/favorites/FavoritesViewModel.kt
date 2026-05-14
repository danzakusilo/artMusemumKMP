package dev.danya.museum.feature.artworks.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.danya.museum.core.common.result.Result
import dev.danya.museum.feature.artworks.domain.entity.ArtworkSummary
import dev.danya.museum.feature.artworks.domain.entity.Exhibit
import dev.danya.museum.feature.artworks.domain.usecase.AddArtworkToExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.CreateExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetExhibitsUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetFavoritesUseCase
import dev.danya.museum.feature.artworks.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavorites: GetFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val getExhibits: GetExhibitsUseCase,
    private val addArtworkToExhibit: AddArtworkToExhibitUseCase,
    private val createExhibit: CreateExhibitUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    private val _exhibits = MutableStateFlow<List<Exhibit>>(emptyList())
    val exhibits: StateFlow<List<Exhibit>> = _exhibits.asStateFlow()

    private var allFavorites: List<ArtworkSummary> = emptyList()
    private var sortOrder: SortOrder = SortOrder.DATE_SAVED
    private var departmentFilter: String? = null
    private val selectedIds = mutableSetOf<Int>()

    init {
        observeFavorites()
        observeExhibits()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavorites().collect { result ->
                when (result) {
                    is Result.Success -> {
                        allFavorites = result.data
                        selectedIds.retainAll(allFavorites.map { it.id }.toSet())
                        rebuildState()
                    }
                    is Result.Error -> {
                        _state.value = FavoritesState.Error(result.error.toString())
                    }
                }
            }
        }
    }

    private fun observeExhibits() {
        viewModelScope.launch {
            getExhibits().collect { result ->
                if (result is Result.Success) {
                    _exhibits.value = result.data
                }
            }
        }
    }

    private fun rebuildState() {
        if (allFavorites.isEmpty()) {
            _state.value = FavoritesState.Empty
            return
        }

        val departments = allFavorites.map { it.department }.distinct().sorted()

        val filtered = if (departmentFilter != null) {
            allFavorites.filter { it.department == departmentFilter }
        } else {
            allFavorites
        }

        val sorted = when (sortOrder) {
            SortOrder.DATE_SAVED -> filtered
            SortOrder.TITLE -> filtered.sortedBy { it.title.lowercase() }
            SortOrder.DEPARTMENT -> filtered.sortedBy { it.department.lowercase() }
        }

        _state.value = FavoritesState.Content(
            artworks = sorted,
            sortOrder = sortOrder,
            departmentFilter = departmentFilter,
            availableDepartments = departments,
            selectedIds = selectedIds.toSet(),
        )
    }

    fun onSortOrderChanged(order: SortOrder) {
        sortOrder = order
        rebuildState()
    }

    fun onDepartmentFilterChanged(department: String?) {
        departmentFilter = department
        rebuildState()
    }

    fun onToggleSelection(id: Int) {
        if (!selectedIds.add(id)) selectedIds.remove(id)
        rebuildState()
    }

    fun onSelectAll() {
        val current = _state.value as? FavoritesState.Content ?: return
        selectedIds.addAll(current.artworks.map { it.id })
        rebuildState()
    }

    fun onClearSelection() {
        selectedIds.clear()
        rebuildState()
    }

    fun onUnfavorite(id: Int) {
        viewModelScope.launch {
            toggleFavorite(id)
        }
    }

    fun onUnfavoriteSelected() {
        val ids = selectedIds.toList()
        selectedIds.clear()
        viewModelScope.launch {
            ids.forEach { toggleFavorite(it) }
        }
    }

    fun onAddSelectedToExhibit(exhibitId: Long) {
        val ids = selectedIds.toList()
        selectedIds.clear()
        rebuildState()
        viewModelScope.launch {
            ids.forEach { addArtworkToExhibit(exhibitId, it) }
        }
    }

    fun onCreateExhibit(name: String) {
        viewModelScope.launch {
            createExhibit(name)
        }
    }
}