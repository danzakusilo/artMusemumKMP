package dev.danya.museum.feature.artworks.ui.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.danya.museum.core.ui.component.ArtworkCard
import dev.danya.museum.feature.artworks.domain.entity.ArtworkSummary
import dev.danya.museum.feature.artworks.domain.entity.Exhibit
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoritesScreen(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val exhibits by viewModel.exhibits.collectAsState()
    var showExhibitSheet by remember { mutableStateOf(false) }

    when (val state = state) {
        is FavoritesState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is FavoritesState.Empty -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No favorites yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        is FavoritesState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Something went wrong", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {}) { Text("Retry") }
                }
            }
        }
        is FavoritesState.Content -> {
            FavoritesContent(
                state = state,
                onNavigateToDetail = onNavigateToDetail,
                onSortOrderChanged = viewModel::onSortOrderChanged,
                onDepartmentFilterChanged = viewModel::onDepartmentFilterChanged,
                onToggleSelection = viewModel::onToggleSelection,
                onSelectAll = viewModel::onSelectAll,
                onClearSelection = viewModel::onClearSelection,
                onUnfavoriteSelected = viewModel::onUnfavoriteSelected,
                onShowExhibitSheet = { showExhibitSheet = true },
            )
        }
    }

    if (showExhibitSheet) {
        ExhibitBottomSheet(
            exhibits = exhibits,
            onDismiss = { showExhibitSheet = false },
            onExhibitSelected = { exhibitId ->
                viewModel.onAddSelectedToExhibit(exhibitId)
                showExhibitSheet = false
            },
            onCreateExhibit = viewModel::onCreateExhibit,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesContent(
    state: FavoritesState.Content,
    onNavigateToDetail: (Int) -> Unit,
    onSortOrderChanged: (SortOrder) -> Unit,
    onDepartmentFilterChanged: (String?) -> Unit,
    onToggleSelection: (Int) -> Unit,
    onSelectAll: () -> Unit,
    onClearSelection: () -> Unit,
    onUnfavoriteSelected: () -> Unit,
    onShowExhibitSheet: () -> Unit,
) {
    val inSelectionMode = state.selectedIds.isNotEmpty()

    Scaffold(
        topBar = {
            if (inSelectionMode) {
                SelectionTopBar(
                    selectedCount = state.selectedIds.size,
                    onClearSelection = onClearSelection,
                    onSelectAll = onSelectAll,
                    onUnfavorite = onUnfavoriteSelected,
                    onAddToExhibit = onShowExhibitSheet,
                )
            } else {
                TopAppBar(title = { Text("Favorites") })
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            FilterRow(
                sortOrder = state.sortOrder,
                departmentFilter = state.departmentFilter,
                availableDepartments = state.availableDepartments,
                onSortOrderChanged = onSortOrderChanged,
                onDepartmentFilterChanged = onDepartmentFilterChanged,
            )
            FavoritesList(
                artworks = state.artworks,
                selectedIds = state.selectedIds,
                inSelectionMode = inSelectionMode,
                onNavigateToDetail = onNavigateToDetail,
                onToggleSelection = onToggleSelection,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectionTopBar(
    selectedCount: Int,
    onClearSelection: () -> Unit,
    onSelectAll: () -> Unit,
    onUnfavorite: () -> Unit,
    onAddToExhibit: () -> Unit,
) {
    TopAppBar(
        title = { Text("$selectedCount selected") },
        navigationIcon = {
            IconButton(onClick = onClearSelection) {
                Icon(Icons.Default.Close, contentDescription = "Clear selection")
            }
        },
        actions = {
            IconButton(onClick = onSelectAll) {
                Icon(Icons.Default.Done, contentDescription = "Select all")
            }
            IconButton(onClick = onAddToExhibit) {
                Icon(Icons.Default.Add, contentDescription = "Add to exhibit")
            }
            IconButton(onClick = onUnfavorite) {
                Icon(Icons.Default.Delete, contentDescription = "Remove from favorites")
            }
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterRow(
    sortOrder: SortOrder,
    departmentFilter: String?,
    availableDepartments: List<String>,
    onSortOrderChanged: (SortOrder) -> Unit,
    onDepartmentFilterChanged: (String?) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SortOrder.entries.forEach { order ->
            FilterChip(
                selected = sortOrder == order,
                onClick = { onSortOrderChanged(order) },
                label = {
                    Text(
                        when (order) {
                            SortOrder.DATE_SAVED -> "Recent"
                            SortOrder.TITLE -> "Title"
                            SortOrder.DEPARTMENT -> "Department"
                        },
                    )
                },
            )
        }
        DepartmentFilterChip(
            selected = departmentFilter,
            departments = availableDepartments,
            onSelected = onDepartmentFilterChanged,
        )
    }
}

@Composable
private fun DepartmentFilterChip(
    selected: String?,
    departments: List<String>,
    onSelected: (String?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        FilterChip(
            selected = selected != null,
            onClick = {
                if (selected != null) onSelected(null) else expanded = true
            },
            label = { Text(selected ?: "All departments") },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            departments.forEach { dept ->
                DropdownMenuItem(
                    text = { Text(dept) },
                    onClick = {
                        onSelected(dept)
                        expanded = false
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoritesList(
    artworks: List<ArtworkSummary>,
    selectedIds: Set<Int>,
    inSelectionMode: Boolean,
    onNavigateToDetail: (Int) -> Unit,
    onToggleSelection: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(artworks, key = { it.id }) { artwork ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {
                            if (inSelectionMode) {
                                onToggleSelection(artwork.id)
                            } else {
                                onNavigateToDetail(artwork.id)
                            }
                        },
                        onLongClick = { onToggleSelection(artwork.id) },
                    ),
            ) {
                if (inSelectionMode) {
                    Checkbox(
                        checked = artwork.id in selectedIds,
                        onCheckedChange = null,
                    )
                    Spacer(Modifier.width(8.dp))
                }
                ArtworkCard(
                    title = artwork.title,
                    imageUrl = artwork.primaryImageUrl,
                    artistName = artwork.artistName,
                    objectDate = artwork.objectDate,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExhibitBottomSheet(
    exhibits: List<Exhibit>,
    onDismiss: () -> Unit,
    onExhibitSelected: (Long) -> Unit,
    onCreateExhibit: (String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    var newExhibitName by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
        ) {
            Text(
                text = "Add to exhibit",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = newExhibitName,
                    onValueChange = { newExhibitName = it },
                    placeholder = { Text("New exhibit name") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(8.dp))
                TextButton(
                    onClick = {
                        if (newExhibitName.isNotBlank()) {
                            onCreateExhibit(newExhibitName.trim())
                            newExhibitName = ""
                        }
                    },
                    enabled = newExhibitName.isNotBlank(),
                ) {
                    Text("Create")
                }
            }

            if (exhibits.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            exhibits.forEach { exhibit ->
                ListItem(
                    headlineContent = { Text(exhibit.name) },
                    supportingContent = {
                        Text("${exhibit.artworkCount} artwork${if (exhibit.artworkCount != 1) "s" else ""}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExhibitSelected(exhibit.id) },
                    tonalElevation = 0.dp,
                )
            }
        }
    }
}
