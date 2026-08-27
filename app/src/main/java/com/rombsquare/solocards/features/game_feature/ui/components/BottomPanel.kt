package com.rombsquare.solocards.features.game_feature.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import com.rombsquare.solocards.core.domain.models.CardSide
import com.rombsquare.solocards.features.game_feature.ui.models.UiEvent
import com.rombsquare.solocards.features.game_feature.ui.models.UiState

@Composable
fun BottomPanel(
    focusRequester: FocusRequester,
    uiState: UiState,
    onEvent: (UiEvent) -> Unit,
) {
    when (uiState.cardSide) {
        CardSide.Question -> {
            BottomQuestionPanel(
                uiState = uiState,
                onAnswer = { onEvent(UiEvent.MakeAnswer(it)) },
                focusRequester = focusRequester
            )
        }

        CardSide.Answer -> {
            BottomAnswerPanel(
                uiState = uiState,
                onAnswer = { onEvent(UiEvent.MakeAnswer(it)) }
            )
        }
    }
}