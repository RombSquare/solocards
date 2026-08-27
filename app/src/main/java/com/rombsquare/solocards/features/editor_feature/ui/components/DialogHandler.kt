package com.rombsquare.solocards.features.editor_feature.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rombsquare.solocards.R
import com.rombsquare.solocards.core.ui.utils.components.WarnDialog
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.CodeDialog
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.HistoryDialog
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.PlayDialog
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.ScriptTutorialDialog
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.SettingsDialog
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.props_dialog.PropsDialog
import com.rombsquare.solocards.features.editor_feature.ui.models.Dialog
import com.rombsquare.solocards.features.editor_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.editor_feature.ui.models.UiState

@Composable
fun DialogHandler(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
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
}