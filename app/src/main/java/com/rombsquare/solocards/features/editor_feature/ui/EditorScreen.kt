package com.rombsquare.solocards.features.editor_feature.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.CardValidationError
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme
import com.rombsquare.solocards.features.editor_feature.ui.components.DialogHandler
import com.rombsquare.solocards.features.editor_feature.ui.components.EditorContent
import com.rombsquare.solocards.features.editor_feature.ui.components.EditorTopBar
import com.rombsquare.solocards.features.editor_feature.ui.models.SnackbarMessage
import com.rombsquare.solocards.features.editor_feature.ui.models.UiEffect
import com.rombsquare.solocards.features.editor_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.editor_feature.ui.models.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    uiState: UiState = UiState(),
    onEvent: (UiEvent) -> Unit = {},
    uiEffect: Flow<UiEffect> = flowOf(),
    onGame: (GameMode, Long) -> Unit = {_, _ ->},
    onHome: () -> Unit = {},
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    if (uiState.moveToGame) {
        onGame(uiState.chosenMode, uiState.quizId!!)
    }

    // For transition animation
    val visibleState = remember { MutableTransitionState(false) }
    LaunchedEffect(Unit) {
        visibleState.targetState = true
    }

    LaunchedEffect(uiEffect) {
        uiEffect.collect { effect ->
            when (effect) {
                is UiEffect.ShowSnackbar -> {
                    val text = when(effect.message) {
                        SnackbarMessage.CannotDeleteSingleCard -> context.getString(R.string.cannot_delete_single_card)
                        SnackbarMessage.CardCreated -> context.getString(R.string.card_created)
                        SnackbarMessage.CardIsDeleted -> context.getString(R.string.card_is_deleted)
                        is SnackbarMessage.Code ->
                            "${context.getString(R.string.question)}: ${effect.message.question}\n\n${context.getString(R.string.answer)}: ${effect.message.answer}"
                        is SnackbarMessage.CodeError -> "${context.getString(R.string.error)}: ${effect.message.reason}"
                        SnackbarMessage.HistoryCleared -> context.getString(R.string.history_cleared)
                        is SnackbarMessage.OnFailedValidation ->
                            when (effect.message.validationError) {
                                CardValidationError.EmptyQuestionAnswer -> context.getString(R.string.empty_question_answer)
                                CardValidationError.EmptyOptions -> context.getString(R.string.empty_options)
                                CardValidationError.NotEnoughOptions -> context.getString(R.string.not_enough_options)
                                CardValidationError.NoAllowedModes -> context.getString(R.string.no_allowed_modes)
                                CardValidationError.TooBigQuestionAnswer -> context.getString(R.string.too_big_question_answer)
                            }
                    }

                    snackbarHostState.showSnackbar(
                        message = text,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    DialogHandler(uiState, onEvent)

    Scaffold(
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
        topBar = {
            EditorTopBar(
                onHome = onHome,
                onHistory = { onEvent(UiEvent.HistoryClicked) },
                onPlay = { onEvent(UiEvent.PlayClicked) },
                onSettings = { onEvent(UiEvent.SettingsClicked) }
            )
        },
        content = { paddingValues ->
            AnimatedVisibility(
                visibleState = visibleState,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                EditorContent(
                    paddingValues = paddingValues,
                    uiState = uiState,
                    onEvent = onEvent
                )
            }
        }
    )
}

@Preview
@Composable
fun EditorScreenPreview() {
    SolocardsTheme {
        EditorScreen()
    }
}