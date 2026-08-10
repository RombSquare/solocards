package com.rombsquare.solocards.ui.screens.menu

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.models.Section
import com.rombsquare.solocards.ui.screens.menu.components.CreateQuizDialog
import com.rombsquare.solocards.ui.screens.menu.components.QuizList
import com.rombsquare.solocards.ui.screens.menu.components.RenameQuizDialog
import com.rombsquare.solocards.ui.screens.menu.components.RestoreQuizDialog
import com.rombsquare.solocards.ui.screens.menu.components.TagDialog
import com.rombsquare.solocards.ui.screens.menu.components.WarnDialog
import com.rombsquare.solocards.ui.screens.menu.models.Dialog
import com.rombsquare.solocards.ui.screens.menu.models.QuizSortingMethod
import com.rombsquare.solocards.ui.screens.menu.models.UiEvent
import com.rombsquare.solocards.ui.screens.menu.models.UiState
import com.rombsquare.solocards.ui.theme.SolocardsTheme
import com.rombsquare.solocards.ui.utils.LabeledRadiobutton
import kotlinx.coroutines.launch

@Composable
fun MenuDrawerItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = label,
                fontWeight = FontWeight.SemiBold
            )
        },
        selected = selected,
        icon = { Icon(icon, label) },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
            unselectedTextColor = MaterialTheme.colorScheme.onBackground,
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedContainerColor = MaterialTheme.colorScheme.onPrimary,
            selectedTextColor = MaterialTheme.colorScheme.primary
        ),
        onClick = onClick,
    )
}

@Composable
fun MenuDrawer(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit,
    drawerState: DrawerState,
) {
    val scope = rememberCoroutineScope()

    Text(
        text = "Solocards",
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground,
    )

    Spacer(Modifier.size(16.dp))

    MenuDrawerItem(
        icon = Icons.Outlined.Category,
        label = "Quizzes",
        selected = uiState.section == Section.Everything,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.SelectSection(Section.Everything))
            }
        }
    )

    MenuDrawerItem(
        icon = Icons.Default.FavoriteBorder,
        label = "Favorites",
        selected = uiState.section == Section.Favorite,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.SelectSection(Section.Favorite))
            }
        }
    )

    MenuDrawerItem(
        icon = Icons.Outlined.Delete,
        label = "Trash",
        selected = uiState.section == Section.Trash,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.SelectSection(Section.Trash))
            }
        }
    )

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 20.dp),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    )

    Text(
        modifier = Modifier.padding(8.dp),
        text = "Tags",
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
    )

    if (uiState.tags.isNotEmpty()) {
        uiState.tags.forEach { tag ->
            MenuDrawerItem(
                icon = Icons.Outlined.Folder,
                label = tag,
                selected = uiState.section == Section.Tag(tag),
                onClick = {
                    scope.launch {
                        drawerState.close()
                        onEvent(UiEvent.SelectSection(Section.Tag(tag)))
                    }
                }
            )
        }
    } else {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Long-press the quiz and tap on hashtag button to set tags for the quiz",
            color = MaterialTheme.colorScheme.primary,
        )
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 20.dp),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
    )

    MenuDrawerItem(
        icon = Icons.Outlined.Archive,
        label = "Archive",
        selected = uiState.section == Section.Archive,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.SelectSection(Section.Archive))
            }
        }
    )

    MenuDrawerItem(
        icon = Icons.Outlined.Settings,
        label = "Settings",
        selected = false,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.SelectSection(Section.Everything))
            }
        }
    )

    MenuDrawerItem(
        icon = Icons.Outlined.HelpOutline,
        label = "Help",
        selected = false,
        onClick = {
            scope.launch {
                drawerState.close()
                onEvent(UiEvent.SelectSection(Section.Everything))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    uiState: UiState = UiState(),
    onEvent: (UiEvent) -> Unit = {},
    onEditor: (Long) -> Unit = {},
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (uiState.showSortingSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            sheetState = sheetState,
            onDismissRequest = { onEvent(UiEvent.HideSortingSheet) },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = "Sort by",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortingMethod == QuizSortingMethod.ByName,
                    onClick = { onEvent(UiEvent.OnSortOptionChosen(QuizSortingMethod.ByName)) },
                    label = "Name"
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortingMethod == QuizSortingMethod.ByDateCreated,
                    onClick = { onEvent(UiEvent.OnSortOptionChosen(QuizSortingMethod.ByDateCreated)) },
                    label = "Date created"
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortingMethod == QuizSortingMethod.ByDateModified,
                    onClick = { onEvent(UiEvent.OnSortOptionChosen(QuizSortingMethod.ByDateModified)) },
                    label = "Date modified"
                )
            }
        }
    }

    when (uiState.dialog) {
        Dialog.CreateQuiz -> {
            CreateQuizDialog(
                onDismiss = {
                    onEvent(UiEvent.HideDialog)
                },
                onConfirm = { name ->
                    onEvent(UiEvent.CreateQuiz(name))
                }
            )
        }

        Dialog.RenameQuiz -> {
            RenameQuizDialog(
                onDismiss = {
                    onEvent(UiEvent.HideDialog)
                },
                onConfirm = {
                    onEvent(UiEvent.RenameQuiz(it))
                }
            )
        }

        Dialog.DeleteWarning -> {
            WarnDialog(
                message = "Are you sure you want to delete this quiz permanently?",
                onConfirm = {
                    onEvent(UiEvent.DeleteForever)
                },
                onDismiss = {
                    onEvent(UiEvent.HideDialog)
                }
            )
        }

        Dialog.ClearTrash -> {
            WarnDialog(
                message = "Are you sure you want to delete trashed quizzes permanently?",
                onConfirm = { onEvent(UiEvent.ClearTrash) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.RestoreQuiz -> {
            RestoreQuizDialog(
                onConfirm = { onEvent(UiEvent.RestoreQuiz) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.TagDialog -> {
            TagDialog(
                initialTags = uiState.selectedQuiz!!.tags,
                onTagAdd = { onEvent(UiEvent.AddTag(it)) },
                onTagRemove = { onEvent(UiEvent.RemoveTag(it)) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        null -> {}
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            val scrollState = rememberScrollState()

            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .verticalScroll(scrollState)
                ) {
                    MenuDrawer(uiState, onEvent, drawerState)
                }

            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = { padding ->
                QuizList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(8.dp),
                    quizzes = uiState.quizzes,
                    onQuizClick = { quiz ->
                        if (uiState.selectedQuiz != null) {
                            if (uiState.selectedQuiz == quiz) {
                                onEvent(UiEvent.UnselectQuiz)
                            } else {
                                onEvent(UiEvent.SelectQuiz(quiz))
                            }
                        } else {
                            if (uiState.section is Section.Trash) {
                                onEvent(UiEvent.QuizClickedInTrash(quiz))
                            } else {
                                onEditor(quiz.id)
                            }

                        }
                    },
                    onFavClicked = { onEvent(UiEvent.FavClicked(it)) },
                    onLongQuizClick = { quiz ->
                        onEvent(UiEvent.SelectQuiz(quiz))
                    },
                    onArchived = { onEvent(UiEvent.OnArchived(it)) },
                    selectedQuiz = uiState.selectedQuiz
                )
            },

            topBar = {
                AnimatedContent(
                    targetState = uiState.selectedQuiz?.id,
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { targetCard ->
                    if (targetCard != null) {
                        TopAppBar(
                            title = { Text("Quiz selected") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                            ),

                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        onEvent(UiEvent.UnselectQuiz)
                                    }
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Close")
                                }
                            },

                            actions = {
                                Row {
                                    if (uiState.section !is Section.Trash) {
                                        IconButton(
                                            onClick = {
                                                onEvent(UiEvent.TagIconClicked)
                                            }
                                        ) {
                                            Icon(Icons.Default.Tag, contentDescription = "Edit tags")
                                        }

                                        IconButton(
                                            onClick = {
                                                onEvent(UiEvent.ShowRenameQuizDialog)
                                            }
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = "Rename")
                                        }
                                    }

                                    IconButton(
                                        onClick = {
                                            onEvent(UiEvent.OnDeleteClicked)
                                        }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Search")
                                    }
                                }
                            }
                        )
                    } else {
                        // Normal bar
                        TopAppBar(
                            title = { Text(uiState.topBarTitle) },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Menu, "Drawer")
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
                                            contentDescription = "Sort")
                                    }
                                }

                                AnimatedVisibility(uiState.searchMode) {
                                    TextField(
                                        modifier = Modifier.width(180.dp),
                                        value = uiState.searchText,
                                        singleLine = true,
                                        onValueChange = {
                                            onEvent(UiEvent.OnSearch(it))
                                        },
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent
                                        ),
                                        placeholder = { Text("Search...") }
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        onEvent(UiEvent.OnSearchClicked)
                                    }
                                ) {
                                    Icon(
                                        imageVector = if(uiState.searchMode) Icons.Default.Close else Icons.Default.Search,
                                        contentDescription = "Search")
                                }
                            }
                        )
                    }
                }
            },

            floatingActionButton = {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = { onEvent(UiEvent.FabClicked) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = if (uiState.section == Section.Trash) Icons.Filled.Delete else Icons.Filled.Add,
                        contentDescription = if (uiState.section == Section.Trash) "Clear trash" else "Create quiz",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun MenuScreenPreview() {
    SolocardsTheme {
        MenuScreen(
            uiState = UiState(
                quizzes = listOf(
                    Quiz(0, "Basic Algebra"),
                    Quiz(1, "Calculus III"),
                    Quiz(2, "Linked Lists"),
                    Quiz(3, "Numbers to 100"),
                    Quiz(4, "Multiplication table")
                ),
                tags = listOf("Biology", "Math", "Programming", "Countries", "Cooking")
            )
        )
    }
}