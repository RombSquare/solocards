package com.rombsquare.solocards.features.menu_feature.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.Section
import com.rombsquare.solocards.features.menu_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.menu_feature.ui.models.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarContent(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()

    AnimatedContent(
        targetState = uiState.selectedQuiz?.id,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { targetCard ->
        if (targetCard != null) {
            TopAppBar(
                title = { Text(stringResource(R.string.quiz_selected)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),

                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(UiEvent.UnselectQuiz)
                        }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
                    }
                },

                actions = {
                    if (uiState.section !is Section.Trash) {
                        IconButton(
                            onClick = {
                                onEvent(UiEvent.TagIconClicked)
                            }
                        ) {
                            Icon(Icons.Default.Tag, contentDescription = stringResource(R.string.edit_tags))
                        }
                    }

                    IconButton(
                        onClick = {
                            onEvent(UiEvent.OnDeleteClicked)
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.search))
                    }

                    if (uiState.section !is Section.Trash) {
                        QuizDropdownMenu(
                            onRename = { onEvent(UiEvent.ShowRenameQuizDialog) },
                            onArchive = { onEvent(UiEvent.OnArchived(uiState.selectedQuiz!!)) },
                            onExport = { onEvent(UiEvent.ExportQuizLocally) },
                            onShare = { onEvent(UiEvent.ShareQuiz) },
                            isQuizArchived = uiState.selectedQuiz?.isArchived == true
                        )
                    }
                }
            )
        } else {
            // Normal bar
            TopAppBar(
                title = {
                    if (!uiState.searchMode)
                        Text(
                            text = when (uiState.section) {
                                Section.Everything -> stringResource(R.string.quizzes)
                                Section.Favorite -> stringResource(R.string.favs)
                                Section.Trash -> stringResource(R.string.trash)
                                Section.Archive -> stringResource(R.string.archive)
                                is Section.Tag -> uiState.section.tag
                            },
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Menu, stringResource(R.string.open_drawer))
                    }
                },
                actions = {
                    if (!uiState.searchMode) {
                        IconButton(
                            onClick = {
                                onEvent(UiEvent.OnSortIconClicked)
                            }
                        ) {
                            Icon(
                                modifier = Modifier.graphicsLayer(scaleX = -1f),
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = stringResource(R.string.sort))
                        }
                    }

                    AnimatedVisibility(uiState.searchMode) {
                        TextField(
                            modifier = Modifier.width(260.dp),
                            value = uiState.searchText,
                            singleLine = true,
                            onValueChange = {
                                onEvent(UiEvent.OnSearch(it))
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            ),
                            placeholder = { Text(stringResource(R.string.search_ellipsis)) }
                        )
                    }

                    IconButton(
                        onClick = {
                            onEvent(UiEvent.OnSearchClicked)
                        }
                    ) {
                        Icon(
                            imageVector = if(uiState.searchMode) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = stringResource(R.string.search))
                    }
                }
            )
        }
    }
}
