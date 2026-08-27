package com.rombsquare.solocards.features.game_feature.ui.components

import androidx.compose.runtime.Composable
import com.rombsquare.solocards.features.game_feature.ui.components.dialogs.EndDialog
import com.rombsquare.solocards.features.game_feature.ui.components.dialogs.ErrorDialog
import com.rombsquare.solocards.features.game_feature.ui.components.dialogs.ExitDialog
import com.rombsquare.solocards.features.game_feature.ui.components.dialogs.SatisfactionDialog
import com.rombsquare.solocards.features.game_feature.ui.models.Dialog
import com.rombsquare.solocards.features.game_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.game_feature.ui.models.UiState

@Composable
fun DialogHandler(
    onHome: () -> Unit,
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    when (val dialog = uiState.dialog) {
        is Dialog.Error -> {
            ErrorDialog(
                reason = dialog.reason,
                wrongCard = dialog.wrongCard,
                onDismiss = onHome
            )
        }

        is Dialog.End -> {
            EndDialog(
                score = dialog.score,
                correct = dialog.correct,
                maybeCorrect = dialog.maybeCorrect,
                incorrect = dialog.incorrect,
                totalCards = uiState.cardCount,
                onDismiss = { onEvent(UiEvent.ShowSatisfactionDialog) }
            )
        }

        Dialog.Satisfaction -> {
            SatisfactionDialog(
                onSelect = { onEvent(UiEvent.OnSatisfactionSelect(it)) }
            )
        }

        Dialog.ExitDialog -> {
            ExitDialog(
                onConfirm = { onEvent(UiEvent.FinishGame) },
                onDismiss = { onEvent(UiEvent.HideDialog) }
            )
        }

        null -> {}
    }
}