package com.rombsquare.solocards.ui.screens.menu

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.GetApp
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.models.Section
import com.rombsquare.solocards.ui.screens.menu.components.dialogs.CreateQuizDialog
import com.rombsquare.solocards.ui.screens.menu.components.QuizList
import com.rombsquare.solocards.ui.screens.menu.components.dialogs.RenameQuizDialog
import com.rombsquare.solocards.ui.screens.menu.components.dialogs.RestoreQuizDialog
import com.rombsquare.solocards.ui.screens.menu.components.dialogs.TagDialog
import com.rombsquare.solocards.ui.screens.menu.components.dialogs.WarnDialog
import com.rombsquare.solocards.ui.screens.menu.models.Dialog
import com.rombsquare.solocards.domain.models.QuizSortMethod
import com.rombsquare.solocards.domain.models.SortDirection
import com.rombsquare.solocards.ui.screens.menu.components.dialogs.SettingsDialog
import com.rombsquare.solocards.ui.screens.menu.models.UiEffect
import com.rombsquare.solocards.ui.screens.menu.models.UiEvent
import com.rombsquare.solocards.ui.screens.menu.models.UiState
import com.rombsquare.solocards.ui.theme.SolocardsTheme
import com.rombsquare.solocards.ui.utils.components.LabeledCheckbox
import com.rombsquare.solocards.ui.utils.components.LabeledRadiobutton
import com.rombsquare.solocards.ui.utils.components.TextWithIcon
import com.rombsquare.solocards.ui.utils.readJsonFromFile
import com.rombsquare.solocards.ui.utils.shareTextAsJson
import com.rombsquare.solocards.ui.utils.writeJsonToFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
                onEvent(UiEvent.SettingsClicked)
            }
        }
    )

    MenuDrawerItem(
        icon = Icons.Outlined.GetApp,
        label = "Import quiz",
        selected = false,
        onClick = {
            scope.launch {
                onEvent(UiEvent.ImportQuizLocally)
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
fun MenuBottomSheetHandler(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit,
    sheetState: SheetState
) {
    if (uiState.showSortingSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            sheetState = sheetState,
            onDismissRequest = { onEvent(UiEvent.HideSortingSheet) },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = "Sort by",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortOptions.method == QuizSortMethod.ByName,
                    onClick = { onEvent(UiEvent.OnSortMethodChosen(QuizSortMethod.ByName)) },
                    label = "Name"
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortOptions.method == QuizSortMethod.ByDateCreated,
                    onClick = { onEvent(UiEvent.OnSortMethodChosen(QuizSortMethod.ByDateCreated)) },
                    label = "Date created"
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortOptions.method == QuizSortMethod.ByDateModified,
                    onClick = { onEvent(UiEvent.OnSortMethodChosen(QuizSortMethod.ByDateModified)) },
                    label = "Date modified"
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortOptions.direction == SortDirection.Ascending,
                    onClick = { onEvent(UiEvent.OnSortDirectionChosen(SortDirection.Ascending)) },
                    label = "Ascending"
                )

                LabeledRadiobutton(
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.sortOptions.direction == SortDirection.Descending,
                    onClick = { onEvent(UiEvent.OnSortDirectionChosen(SortDirection.Descending)) },
                    label = "Descending"
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                LabeledCheckbox(
                    modifier = Modifier.fillMaxWidth(),
                    checked = uiState.sortOptions.moveFavoritesToTop,
                    onCheckedChange = { onEvent(UiEvent.OnMoveFavoritesToTopToggle) },
                    label = "Move favorites top top"
                )

                Spacer(Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun DialogHandler(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
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

        Dialog.Tag -> {
            TagDialog(
                initialTags = uiState.selectedQuiz!!.tags,
                onTagAdd = { onEvent(UiEvent.AddTag(it)) },
                onTagRemove = { onEvent(UiEvent.RemoveTag(it)) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.Settings -> {
            SettingsDialog(
                onImportProgress = { onEvent(UiEvent.ImportClicked) },
                onExportProgress = { onEvent(UiEvent.ExportProgressLocally) },
                onResetProgress = { onEvent(UiEvent.ResetClicked) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.ResetProgress -> {
            WarnDialog(
                message = "Are you sure you want to reset progress? This process is irreversible",
                onConfirm = { onEvent(UiEvent.ResetProgress) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.ImportProgress -> {
            WarnDialog(
                message = "By clicking yes, all current progress will be lost",
                onConfirm = { onEvent(UiEvent.ImportProgressLocally) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        null -> {}
    }
}

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
                    if (uiState.section !is Section.Trash) {
                        IconButton(
                            onClick = {
                                onEvent(UiEvent.TagIconClicked)
                            }
                        ) {
                            Icon(Icons.Default.Tag, contentDescription = "Edit tags")
                        }
                    }

                    IconButton(
                        onClick = {
                            onEvent(UiEvent.OnDeleteClicked)
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Search")
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
                            text = uiState.topBarTitle,
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
}

@Composable
fun ToastHandler(
    message: String,
    onToastShown: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            onToastShown()
        }
    }
}

@Composable
fun QuizDropdownMenu(
    onRename: () -> Unit,
    onArchive: () -> Unit,
    onExport: () -> Unit,
    onShare: () -> Unit,
    isQuizArchived: Boolean,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Open dropdown menu"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            DropdownMenuItem(
                text = {
                    TextWithIcon(
                        icon = { Icon(Icons.Default.Edit, "Rename") },
                        text = { Text("Rename") }
                    )
                },
                onClick = onRename
            )

            DropdownMenuItem(
                text = {
                    TextWithIcon(
                        icon = { Icon(if (isQuizArchived) Icons.Default.Unarchive else Icons.Default.Archive, "Archive") },
                        text = { Text(if (isQuizArchived) "Unarchive" else "Archive") }
                    )
                },
                onClick = onArchive
            )

            DropdownMenuItem(
                text = {
                    TextWithIcon(
                        icon = { Icon(Icons.Default.Upload, "Export") },
                        text = { Text("Export") }
                    )
                },
                onClick = onExport
            )

            DropdownMenuItem(
                text = {
                    TextWithIcon(
                        icon = { Icon(Icons.Default.Share, "Share") },
                        text = { Text("Share") }
                    )
                },
                onClick = onShare
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    uiState: UiState = UiState(),
    onEvent: (UiEvent) -> Unit = {},
    uiEffect: Flow<UiEffect> = flowOf(),
    onEditor: (Long) -> Unit = {},
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Handle quiz/data export
    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            writeJsonToFile(context, selectedUri, uiState.serializedData)
        }
    }

    // Handle quiz/data import
    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            try {
                val jsonString = readJsonFromFile(context, selectedUri)
                onEvent(UiEvent.ObtainImportedData(jsonString!!))

                Log.d("SolocardsTest", "Successfully read the json! The content: $jsonString")
            } catch (e: Exception) {

            }
        }
    }

    ToastHandler(uiState.toastMessage) { onEvent(UiEvent.OnToastShown) }
    MenuBottomSheetHandler(uiState, onEvent, sheetState)
    DialogHandler(uiState, onEvent)

    LaunchedEffect(Unit) {
        uiEffect.collect { effect ->
            when (effect) {
                is UiEffect.CreateDocument -> {
                    createDocumentLauncher.launch(effect.name)
                }

                UiEffect.OpenDocument -> {
                    openDocumentLauncher.launch(arrayOf("application/json", "*/*"))
                }

                is UiEffect.ShareJson -> {
                    try {
                        shareTextAsJson(context, effect.jsonString, effect.name)
                    } catch (_: Exception) {
                        Toast.makeText(context, "Cannot share it", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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
                        Log.d("SolocardsTest", "Quiz long-tapped: ${quiz.id}")
                    },
                    onArchived = { onEvent(UiEvent.OnArchived(it)) },
                    selectedQuiz = uiState.selectedQuiz
                )
            },

            topBar = {
                TopAppBarContent(uiState, onEvent, drawerState)
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