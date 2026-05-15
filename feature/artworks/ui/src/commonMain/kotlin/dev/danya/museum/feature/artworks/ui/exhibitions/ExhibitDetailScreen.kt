package dev.danya.museum.feature.artworks.ui.exhibitions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.danya.museum.core.ui.component.ArtworkCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExhibitDetailScreen(
    exhibitId: Long,
    exhibitName: String,
    onBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: ExhibitDetailViewModel = koinViewModel(parameters = { parametersOf(exhibitId) }),
) {
    val state by viewModel.state.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var displayName by remember { mutableStateOf(exhibitName) }
    var editFieldValue by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isEditing) {
                        OutlinedTextField(
                            value = editFieldValue,
                            onValueChange = { editFieldValue = it },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.titleMedium,
                            colors = OutlinedTextFieldDefaults.colors(),
                            modifier = Modifier.focusRequester(focusRequester),
                        )
                    } else {
                        Text(
                            text = displayName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                navigationIcon = {
                    if (isEditing) {
                        IconButton(onClick = { isEditing = false }) {
                            Icon(Icons.Filled.Close, contentDescription = "Cancel")
                        }
                    } else {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                },
                actions = {
                    if (isEditing) {
                        IconButton(
                            onClick = {
                                val trimmed = editFieldValue.text.trim()
                                if (trimmed.isNotBlank()) {
                                    viewModel.onRename(trimmed)
                                    displayName = trimmed
                                }
                                isEditing = false
                            },
                            enabled = editFieldValue.text.isNotBlank(),
                        ) {
                            Icon(Icons.Filled.Check, contentDescription = "Confirm")
                        }
                    } else {
                        IconButton(onClick = {
                            editFieldValue = TextFieldValue(
                                text = displayName,
                                selection = TextRange(displayName.length),
                            )
                            isEditing = true
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Rename")
                        }
                    }
                },
            )
        },
    ) { padding ->
        when (val state = state) {
            is ExhibitDetailState.Loading -> {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            is ExhibitDetailState.Empty -> {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No artworks in this exhibition",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            is ExhibitDetailState.Error -> {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Something went wrong", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = {}) { Text("Retry") }
                    }
                }
            }
            is ExhibitDetailState.Content -> {
                val lazyListState = rememberLazyListState()
                val scope = rememberCoroutineScope()
                val dragDropState = remember(lazyListState) {
                    DragDropState(
                        lazyListState = lazyListState,
                        onMove = viewModel::onMove,
                        onDragEnd = viewModel::onDragEnd,
                        scope = scope,
                    )
                }
                val haptic = LocalHapticFeedback.current

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(
                        state.artworks,
                        key = { _, artwork -> artwork.id },
                    ) { _, artwork ->
                        val isDragging = artwork.id == dragDropState.draggedKey
                        ArtworkCard(
                            title = artwork.title,
                            imageUrl = artwork.primaryImageUrl,
                            artistName = artwork.artistName,
                            objectDate = artwork.objectDate,
                            modifier = Modifier
                                .zIndex(if (isDragging) 1f else 0f)
                                .then(if (!isDragging) Modifier.animateItem() else Modifier)
                                .graphicsLayer {
                                    translationY = dragDropState.offsetForKey(artwork.id)
                                    if (isDragging) {
                                        scaleX = 1.02f
                                        scaleY = 1.02f
                                        shadowElevation = 8f
                                    }
                                }
                                .fillMaxWidth()
                                .pointerInput(artwork.id) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            dragDropState.onDragStart(artwork.id)
                                        },
                                        onDrag = { change, offset ->
                                            change.consume()
                                            dragDropState.onDrag(offset.y)
                                        },
                                        onDragEnd = { dragDropState.onDragStop() },
                                        onDragCancel = { dragDropState.onDragStop() },
                                    )
                                }
                                .clickable { onNavigateToDetail(artwork.id) },
                        )
                    }
                }
            }
        }
    }
}

private class DragDropState(
    private val lazyListState: LazyListState,
    private val onMove: (Int, Int) -> Unit,
    private val onDragEnd: () -> Unit,
    private val scope: CoroutineScope,
) {
    var draggedKey by mutableStateOf<Any?>(null)
        private set

    private var rawDragDelta = 0f
    private var startLayoutOffset = 0
    private var autoScrollJob: Job? = null

    fun offsetForKey(key: Any): Float {
        if (key != draggedKey) return 0f
        val item = lazyListState.layoutInfo.visibleItemsInfo.find { it.key == key } ?: return 0f
        return startLayoutOffset + rawDragDelta - item.offset
    }

    fun onDragStart(key: Any) {
        draggedKey = key
        rawDragDelta = 0f
        val item = lazyListState.layoutInfo.visibleItemsInfo.find { it.key == key }
        startLayoutOffset = item?.offset ?: 0
    }

    fun onDrag(yDelta: Float) {
        rawDragDelta += yDelta
        checkForSwap()
        updateAutoScroll()
    }

    fun onDragStop() {
        autoScrollJob?.cancel()
        autoScrollJob = null
        if (draggedKey != null) onDragEnd()
        draggedKey = null
        rawDragDelta = 0f
    }

    private fun checkForSwap() {
        val items = lazyListState.layoutInfo.visibleItemsInfo
        val draggedItem = items.find { it.key == draggedKey } ?: return
        val visualTop = startLayoutOffset + rawDragDelta
        val visualCenter = visualTop + draggedItem.size / 2

        val target = items.firstOrNull { item ->
            item.key != draggedKey &&
                visualCenter.toInt() in item.offset..(item.offset + item.size)
        } ?: return

        onMove(draggedItem.index, target.index)
    }

    private fun updateAutoScroll() {
        val info = lazyListState.layoutInfo
        val item = info.visibleItemsInfo.find { it.key == draggedKey } ?: return
        val visualTop = startLayoutOffset + rawDragDelta
        val visualBottom = visualTop + item.size
        val viewportHeight = info.viewportEndOffset.toFloat()
        val threshold = viewportHeight * 0.15f

        val speed = when {
            visualBottom > viewportHeight - threshold ->
                ((visualBottom - (viewportHeight - threshold)) / threshold * 20f).coerceAtMost(20f)
            visualTop < threshold ->
                ((visualTop - threshold) / threshold * 20f).coerceAtLeast(-20f)
            else -> 0f
        }

        if (speed == 0f) {
            autoScrollJob?.cancel()
            autoScrollJob = null
            return
        }

        if (autoScrollJob?.isActive == true) return
        autoScrollJob = scope.launch {
            while (isActive) {
                lazyListState.scrollBy(speed)
                delay(16)
            }
        }
    }
}
