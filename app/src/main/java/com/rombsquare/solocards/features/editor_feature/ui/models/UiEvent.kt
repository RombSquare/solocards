package com.rombsquare.solocards.features.editor_feature.ui.models

import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.domain.models.Session
import com.rombsquare.solocards.features.editor_feature.ui.components.dialogs.QuizSettingsValues

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
    data object OnScriptHelpClicked: UiEvent

    data object OpenPropsDialog: UiEvent
    data class UpdateCardProps(val options: List<String>, val optionCount: Int, val occurrences: Int, val allowedModes: List<GameMode>): UiEvent

    data object HideDialog: UiEvent

    data object PlayClicked: UiEvent
    data class OnModeClicked(val mode: GameMode): UiEvent

    // Settings
    data object SettingsClicked: UiEvent
    data class UpdateSettings(val quizSettings: QuizSettingsValues): UiEvent

    // History feature
    data object HistoryClicked: UiEvent
    data class SelectGameResult(val session: Session?): UiEvent
    data object DeleteSelectedGameResult: UiEvent
    data object DeleteAllGameResults: UiEvent
}