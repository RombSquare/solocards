package com.rombsquare.solocards.ui.screens.menu.models

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.models.Section

sealed interface UiEvent {
    data class CreateQuiz(val name: String): UiEvent
    data class SelectQuiz(val quiz: Quiz): UiEvent
    data class RenameQuiz(val name: String): UiEvent
    data class QuizClickedInTrash(val quiz: Quiz): UiEvent
    data object RestoreQuiz: UiEvent

    data object TagIconClicked: UiEvent
    data class AddTag(val newTag: String): UiEvent
    data class RemoveTag(val tagToRemove: String): UiEvent

    data object OnDeleteClicked: UiEvent
    data object ClearTrash: UiEvent
    data object DeleteForever: UiEvent

    data object ShowRenameQuizDialog: UiEvent
    data object HideDialog: UiEvent
    data object UnselectQuiz: UiEvent

    data object FabClicked: UiEvent
    data class SelectSection(val section: Section): UiEvent
    data class FavClicked(val quiz: Quiz): UiEvent

    // Search
    data object OnSearchClicked: UiEvent
    data class OnSearch(val text: String): UiEvent

    // Sort
    data object OnSortIconClicked: UiEvent
    data class OnSortOptionChosen(val sortingMethod: QuizSortingMethod): UiEvent
    data object HideSortingSheet: UiEvent

    data class OnArchived(val quiz: Quiz): UiEvent
}