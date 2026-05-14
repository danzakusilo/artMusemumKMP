package dev.danya.museum.feature.artworks.ui.exhibitions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.danya.museum.core.ui.component.ArtworkCard
import dev.danya.museum.feature.artworks.domain.entity.ExhibitWithPreviews
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExhibitionsTab(
    onNavigateToExhibit: (Long, String) -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: ExhibitionsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    when (val state = state) {
        is ExhibitionsState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ExhibitionsState.Empty -> {
            ExhibitionsEmpty(onCreateExhibit = viewModel::onCreateExhibit)
        }
        is ExhibitionsState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Something went wrong", style = MaterialTheme.typography.bodyLarge)
            }
        }
        is ExhibitionsState.Content -> {
            ExhibitionsContent(
                exhibits = state.exhibits,
                onNavigateToExhibit = { id, name -> onNavigateToExhibit(id, name) },
                onNavigateToDetail = onNavigateToDetail,
                onDeleteExhibit = viewModel::onDeleteExhibit,
                onRenameExhibit = viewModel::onRenameExhibit,
                onCreateExhibit = viewModel::onCreateExhibit,
            )
        }
    }
}

@Composable
private fun ExhibitionsEmpty(onCreateExhibit: (String) -> Unit) {
    var showCreateDialog by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No exhibitions yet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = { showCreateDialog = true }) {
                Text("Create Exhibition")
            }
        }
    }

    if (showCreateDialog) {
        CreateExhibitDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { name ->
                onCreateExhibit(name)
                showCreateDialog = false
            },
        )
    }
}

@Composable
private fun ExhibitionsContent(
    exhibits: List<ExhibitWithPreviews>,
    onNavigateToExhibit: (Long, String) -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onDeleteExhibit: (Long) -> Unit,
    onRenameExhibit: (Long, String) -> Unit,
    onCreateExhibit: (String) -> Unit,
) {
    var showCreateDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(exhibits, key = { it.exhibit.id }) { exhibitWithPreviews ->
            ExhibitRow(
                exhibitWithPreviews = exhibitWithPreviews,
                onNavigateToExhibit = {
                    onNavigateToExhibit(exhibitWithPreviews.exhibit.id, exhibitWithPreviews.exhibit.name)
                },
                onNavigateToDetail = onNavigateToDetail,
                onDelete = { onDeleteExhibit(exhibitWithPreviews.exhibit.id) },
                onRename = { name -> onRenameExhibit(exhibitWithPreviews.exhibit.id, name) },
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                TextButton(onClick = { showCreateDialog = true }) {
                    Text("Create Exhibition")
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateExhibitDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { name ->
                onCreateExhibit(name)
                showCreateDialog = false
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ExhibitRow(
    exhibitWithPreviews: ExhibitWithPreviews,
    onNavigateToExhibit: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onDelete: () -> Unit,
    onRename: (String) -> Unit,
) {
    var showContextMenu by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onNavigateToExhibit,
                    onLongClick = { showContextMenu = true },
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exhibitWithPreviews.exhibit.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${exhibitWithPreviews.exhibit.artworkCount} artwork${if (exhibitWithPreviews.exhibit.artworkCount != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onNavigateToExhibit) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "View exhibition",
                )
            }

            DropdownMenu(
                expanded = showContextMenu,
                onDismissRequest = { showContextMenu = false },
            ) {
                DropdownMenuItem(
                    text = { Text("Rename") },
                    onClick = {
                        showContextMenu = false
                        showRenameDialog = true
                    },
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        showContextMenu = false
                        onDelete()
                    },
                )
            }
        }

        if (exhibitWithPreviews.previewArtworks.isEmpty()) {
            Text(
                text = "Empty exhibition",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    exhibitWithPreviews.previewArtworks,
                    key = { it.id },
                ) { artwork ->
                    ArtworkCard(
                        title = artwork.title,
                        imageUrl = artwork.primaryImageUrl,
                        artistName = artwork.artistName,
                        objectDate = artwork.objectDate,
                        modifier = Modifier.width(160.dp),
                        onClick = { onNavigateToDetail(artwork.id) },
                    )
                }
            }
        }
    }

    if (showRenameDialog) {
        RenameExhibitDialog(
            currentName = exhibitWithPreviews.exhibit.name,
            onDismiss = { showRenameDialog = false },
            onConfirm = { name ->
                onRename(name)
                showRenameDialog = false
            },
        )
    }
}

@Composable
private fun CreateExhibitDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Exhibition") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Exhibition name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name.trim()) },
                enabled = name.isNotBlank(),
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
private fun RenameExhibitDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename Exhibition") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name.trim()) },
                enabled = name.isNotBlank(),
            ) {
                Text("Rename")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}