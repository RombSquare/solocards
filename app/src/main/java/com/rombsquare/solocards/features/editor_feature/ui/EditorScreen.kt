package com.rombsquare.solocards.features.editor_feature.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.domain.models.CardValidationError
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.CodeDialog
import com.rombsquare.solocards.features.menu_feature.ui.components.dialogs.WarnDialog
import com.rombsquare.solocards.features.editor_feature.ui.components.EditableCard
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.HistoryDialog
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.PlayDialog
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.props_dialog.PropsDialog
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.SettingsDialog
import com.rombsquare.solocards.features.editor_feature.ui.models.Dialog
import com.rombsquare.solocards.features.editor_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.editor_feature.ui.models.UiState
import com.rombsquare.solocards.core.ui.theme.SolocardsTheme
import com.rombsquare.solocards.core.ui.utils.components.TextWithIcon
import com.rombsquare.solocards.features.editor_feature.ui.components.CardPager
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.ScriptTutorialDialog
import com.rombsquare.solocards.features.editor_feature.ui.models.SnackbarMessage
import com.rombsquare.solocards.features.editor_feature.ui.models.UiEffect
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

    when (uiState.dialog) {
        Dialog.Code -> {
            CodeDialog(
                code = uiState.currentCard!!.code,
                occurrences = uiState.currentCard.count,
                onSave = { code, occurrences ->
                    onEvent(UiEvent.SaveCode(code, occurrences))
                },
                onRun = { code, occurrences ->
                    onEvent(UiEvent.RunCode(code, occurrences))
                },
                onHelp = {
                    onEvent(UiEvent.OnScriptHelpClicked)
                }
            )
        }

        Dialog.DeleteWarning -> {
            WarnDialog(
                message = stringResource(R.string.card_deletion_warning),
                onConfirm = { onEvent(UiEvent.DeleteCard) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.Play -> {
            PlayDialog(
                onAccept = { mode ->
                    onEvent(UiEvent.OnModeClicked(mode))
                },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.History -> {
            HistoryDialog(
                sessions = uiState.sessions,
                selected = uiState.selectedSession,
                showSatisfaction = uiState.quiz.rateQuiz,
                onSelected = { onEvent(UiEvent.SelectGameResult(it)) },
                onDelete = { onEvent(UiEvent.DeleteSelectedGameResult) },
                onDeleteAll = { onEvent(UiEvent.DeleteAllGameResults) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.Props -> {
            PropsDialog(
                onDismiss = { options, optionCount, occurrences, allowedModes ->
                    onEvent(UiEvent.UpdateCardProps(
                        options,
                        optionCount,
                        occurrences,
                        allowedModes
                    ))
                },
                options = uiState.currentCard!!.options,
                optionCount = uiState.currentCard.optionCount,
                occurrences = uiState.currentCard.count,
                allowedModes = uiState.currentCard.allowedModes,
                onCode = { onEvent(UiEvent.OpenCodeDialog) }
            )
        }

        Dialog.Settings -> {
            SettingsDialog(
                quiz = uiState.quiz,
                onDismiss = { onEvent(UiEvent.UpdateSettings(it)) },
            )
        }

        Dialog.ScriptHelp -> {
            ScriptTutorialDialog(
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        null -> {}
    }

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
            TopAppBar(
                title = { Text(stringResource(R.string.card_editor)) },
                navigationIcon = {
                    IconButton(
                        onClick = onHome,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back_to_menu))
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(UiEvent.HistoryClicked) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = stringResource(R.string.game_history),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    IconButton(
                        onClick = { onEvent(UiEvent.SettingsClicked) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    IconButton(
                        onClick = { onEvent(UiEvent.Play) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = stringResource(R.string.play),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            AnimatedVisibility(
                visibleState = visibleState,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        CardPager(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.6f),
                            currentIndex = uiState.index
                        ) { targetIndex ->
                            val card = uiState.cards.getOrNull(targetIndex)

                            EditableCard(
                                modifier = Modifier
                                    .padding(24.dp)
                                    .fillMaxSize(),
                                cardSide = uiState.cardSide,
                                question = card?.question ?: "",
                                answer = card?.answer ?: "",
                                onValueChange = {
                                    onEvent(UiEvent.OnCardTextChange(it))
                                },
                                onValueAccept = {
                                    onEvent(UiEvent.OnCardTextAccept)
                                },
                                onDelete = { onEvent(UiEvent.ShowDeleteWarning) },
                                onModify = { onEvent(UiEvent.OpenPropsDialog) }
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                modifier = Modifier.height(60.dp),
                                onClick = { onEvent(UiEvent.FlipCard) }
                            ) {
                                TextWithIcon(
                                    text = { Text(stringResource(R.string.flip_card)) },
                                    icon = { Icon(Icons.Default.Loop, stringResource(R.string.flip_card)) }
                                )
                            }
                        }



                        Spacer(Modifier.weight(1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { onEvent(UiEvent.PrevCard) }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.prev_card),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            Text(
                                text = "${uiState.index+1}/${uiState.cardCount}",
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(Modifier.weight(1f))

                            if (uiState.index + 1 == uiState.cardCount) {
                                IconButton(
                                    onClick = { onEvent(UiEvent.CreateCard) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = stringResource(R.string.create_card),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = { onEvent(UiEvent.NextCard) }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = stringResource(R.string.next_card),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    )

}

@Preview(
    //showBackground = false,
    //backgroundColor = 0xFF000000,
    //uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun EditorScreenPreview() {
    SolocardsTheme {
        EditorScreen()
    }
}