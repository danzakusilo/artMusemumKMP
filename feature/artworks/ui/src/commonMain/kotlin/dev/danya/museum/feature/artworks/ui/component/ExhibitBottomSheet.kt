package dev.danya.museum.feature.artworks.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.danya.museum.feature.artworks.domain.entity.Exhibit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExhibitBottomSheet(
    exhibits: List<Exhibit>,
    artworkExhibitIds: Set<Long>,
    onDismiss: () -> Unit,
    onToggleExhibit: (Long) -> Unit,
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
                text = "Exhibits",
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
                val isInExhibit = exhibit.id in artworkExhibitIds
                ListItem(
                    headlineContent = { Text(exhibit.name) },
                    supportingContent = {
                        Text("${exhibit.artworkCount} artwork${if (exhibit.artworkCount != 1) "s" else ""}")
                    },
                    trailingContent = {
                        Checkbox(
                            checked = isInExhibit,
                            onCheckedChange = null,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleExhibit(exhibit.id) },
                    tonalElevation = 0.dp,
                )
            }
        }
    }
}