package com.rombsquare.solocards.ui.screens.editor.models

import com.rombsquare.solocards.domain.models.GameMode
import com.rombsquare.solocards.domain.models.GameResult
import com.rombsquare.solocards.ui.screens.editor.components.dialogs.QuizSettingsValues

sealed interface UiEvent {
    data object NextCard: UiEvent
    data object PrevCard: UiEvent
    data object FlipCard: UiEvent
    data object CreateCard: UiEvent

    data object ShowDeleteWarning: UiEvent
    data object DeleteCard: UiEvent

    data class OnCardTextChange(val newText: String): UiEvent
    data object OnCardTextAccept: UiEvent

    data object OpenCodeDialog: UiEvent
    data class RunCode(val code: String, val occurrences: Int): UiEvent
    data class SaveCode(val code: String, val occurrences: Int): UiEvent

    data object OpenPropsDialog: UiEvent
    data class UpdateCardProps(val optionBlankString: String, val optionCount: Int, val allowedModes: Map<GameMode, Boolean>): UiEvent

    data object ToastMessageShown: UiEvent
    data object HideDialog: UiEvent

    data object Play: UiEvent
    data class OnModeClicked(val mode: GameMode): UiEvent

    // Settings
    data object SettingsClicked: UiEvent
    data class UpdateSettings(val quizSettings: QuizSettingsValues): UiEvent

    // History feature
    data object HistoryClicked: UiEvent
    data class SelectGameResult(val gameResult: GameResult?): UiEvent
    data object DeleteSelectedGameResult: UiEvent
    data object DeleteAllGameResults: UiEvent
}