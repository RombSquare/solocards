package com.rombsquare.solocards.features.menu_feature.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rombsquare.solocards.R
import com.rombsquare.solocards.features.menu_feature.ui.components.dialogs.AboutDialog
import com.rombsquare.solocards.features.menu_feature.ui.components.dialogs.CreateQuizDialog
import com.rombsquare.solocards.features.menu_feature.ui.components.dialogs.RenameQuizDialog
import com.rombsquare.solocards.features.menu_feature.ui.components.dialogs.RestoreQuizDialog
import com.rombsquare.solocards.features.menu_feature.ui.components.dialogs.SettingsDialog
import com.rombsquare.solocards.features.menu_feature.ui.components.dialogs.TagDialog
import com.rombsquare.solocards.features.menu_feature.ui.components.dialogs.WarnDialog
import com.rombsquare.solocards.features.menu_feature.ui.models.Dialog
import com.rombsquare.solocards.features.menu_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.menu_feature.ui.models.UiState

@Composable
fun DialogHandler(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit,
    onCloud: () -> Unit,
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
                currentName = uiState.selectedQuiz?.title ?: "",
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
                message = stringResource(R.string.permanent_quiz_deletion_warning),
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
                message = stringResource(R.string.clear_trash_warning),
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
                onCloud = onCloud,
                onDismiss = { onEvent(UiEvent.HideDialog) },
            )
        }

        Dialog.ResetProgress -> {
            WarnDialog(
                message = stringResource(R.string.reset_data_warning),
                onConfirm = { onEvent(UiEvent.ResetProgress) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.ImportProgress -> {
            WarnDialog(
                message = stringResource(R.string.import_data_warning),
                onConfirm = { onEvent(UiEvent.ImportProgressLocally) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        Dialog.About -> {
            AboutDialog(
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        null -> {}
    }
}