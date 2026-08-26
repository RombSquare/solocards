package com.rombsquare.solocards.features.menu_feature.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.models.QuizValidationError
import com.rombsquare.solocards.core.domain.models.TagValidationError
import com.rombsquare.solocards.core.domain.models.Section
import com.rombsquare.solocards.features.menu_feature.ui.components.DialogHandler
import com.rombsquare.solocards.features.menu_feature.ui.components.MenuBottomSheetHandler
import com.rombsquare.solocards.features.menu_feature.ui.components.MenuDrawer
import com.rombsquare.solocards.features.menu_feature.ui.components.QuizList
import com.rombsquare.solocards.features.menu_feature.ui.components.TopAppBarContent
import com.rombsquare.solocards.features.menu_feature.ui.models.UiEffect
import com.rombsquare.solocards.features.menu_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.menu_feature.ui.models.UiState
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme
import com.rombsquare.solocards.features.menu_feature.ui.models.SnackbarMessage
import com.rombsquare.solocards.features.menu_feature.ui.utils.readJsonFromFile
import com.rombsquare.solocards.features.menu_feature.ui.utils.shareTextAsJson
import com.rombsquare.solocards.features.menu_feature.ui.utils.writeJsonToFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    uiState: UiState = UiState(),
    onEvent: (UiEvent) -> Unit = {},
    uiEffect: Flow<UiEffect> = flowOf(),
    onEditor: (Long) -> Unit = {},
    onFaq: () -> Unit = {},
    onCloud: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // For transition animation
    val visibleState = remember { MutableTransitionState(false) }
    LaunchedEffect(Unit) {
        visibleState.targetState = true
    }

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

    MenuBottomSheetHandler(uiState, onEvent, sheetState)
    DialogHandler(uiState, onEvent, onCloud)

    LaunchedEffect(Unit) {
        uiEffect.collect { effect ->
            when (effect) {
                is UiEffect.CreateDocument -> {
                    createDocumentLauncher.launch(effect.name)
                }

                UiEffect.OpenDocument -> {
                    openDocumentLauncher.launch(arrayOf("application/json"))
                }

                is UiEffect.ShareJson -> {
                    try {
                        shareTextAsJson(context, effect.jsonString, effect.name)
                    } catch (_: Exception) {
                        Toast.makeText(context, context.getString(R.string.share_error), Toast.LENGTH_SHORT).show()
                    }
                }

                is UiEffect.ShowSnackbar -> {

                    val text = when (effect.message) {
                        SnackbarMessage.MovedToTrash -> context.getString(R.string.moved_to_trash)
                        SnackbarMessage.TrashIsCleared -> context.getString(R.string.trash_is_cleared)
                        SnackbarMessage.QuizWasDeleted -> context.getString(R.string.quiz_was_deleted)
                        SnackbarMessage.QuizWasRestored -> context.getString(R.string.quiz_was_restored)
                        SnackbarMessage.QuizWasArchived -> context.getString(R.string.quiz_was_archived)
                        SnackbarMessage.QuizWasUnarchived -> context.getString(R.string.quiz_was_unarchived)
                        SnackbarMessage.LoadedSuccessfully -> context.getString(R.string.loaded_successfully)
                        SnackbarMessage.CannotOpen -> context.getString(R.string.cannot_open)
                        SnackbarMessage.ExportedSuccessfully -> context.getString(R.string.exported_successfully)
                    }

                    val result = snackbarHostState.showSnackbar(
                        message = text,
                        duration = SnackbarDuration.Short,
                        actionLabel = effect.onUndo?.let { context.getString(R.string.undo) }
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        scope.launch {
                            effect.onUndo?.let { it() }
                        }
                    }
                }

                is UiEffect.ShowValidationErrorToast -> {
                    val text = when (effect.validation) {
                        is QuizValidationError ->
                            when (effect.validation) {
                                QuizValidationError.NameIsEmpty -> context.getString(R.string.name_is_empty)
                                QuizValidationError.NameIsTooLong -> context.getString(R.string.name_is_too_long)
                                QuizValidationError.NameExists -> context.getString(R.string.name_exists)
                            }


                        is TagValidationError -> {
                            when (effect.validation) {
                                TagValidationError.EmptyTag -> context.getString(R.string.tag_is_empty)
                                TagValidationError.TagIsTooLong -> context.getString(R.string.tag_is_too_long)
                                TagValidationError.TagExists -> context.getString(R.string.tag_exists)
                                TagValidationError.WrongTagName -> context.getString(R.string.wrong_tag_name)
                                TagValidationError.TooManyTags -> context.getString(R.string.too_many_tags)
                            }
                        }

                        else -> ""
                    }

                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                }

                is UiEffect.GoToEditor -> {
                    onEditor(effect.quizId)
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            val scrollState = rememberScrollState()

            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .verticalScroll(scrollState)
                ) {
                    MenuDrawer(uiState, onEvent, onFaq, drawerState)
                }

            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionColor = MaterialTheme.colorScheme.primary,
                    )
                }
            },
            content = { padding ->
                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = fadeIn(animationSpec = tween(500)),
                    exit = fadeOut(animationSpec = tween(500))
                ) {
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
                }

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
                        contentDescription = if (uiState.section == Section.Trash) stringResource(R.string.clear_trash) else stringResource(R.string.create_quiz),
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