package com.rombsquare.solocards.ui.screens.game.models

import com.rombsquare.solocards.domain.models.Satisfaction

sealed interface UiEvent {
    data object ShowAnswer: UiEvent
    data object ShowSatisfactionDialog: UiEvent

    data class MakeAnswer(val answer: UserAnswer?): UiEvent
    data class OnSatisfactionSelect(val satis: Satisfaction): UiEvent

    data object ExitClicked: UiEvent
    data object FinishGame: UiEvent
    data object HideDialog: UiEvent
}