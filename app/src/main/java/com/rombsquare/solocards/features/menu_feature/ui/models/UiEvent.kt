package com.rombsquare.solocards.features.menu_feature.ui.models

import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.features.menu_feature.domain.models.QuizSortMethod
import com.rombsquare.solocards.features.menu_feature.domain.models.Section
import com.rombsquare.solocards.features.menu_feature.domain.models.SortDirection

sealed interface UiEvent {

    // Basic quiz operations
    data class CreateQuiz(val name: String): UiEvent
    data class SelectQuiz(val quiz: Quiz): UiEvent
    data class RenameQuiz(val name: String): UiEvent
    data class QuizClickedInTrash(val quiz: Quiz): UiEvent
    data object RestoreQuiz: UiEvent

    // Tags
    data object TagIconClicked: UiEvent
    data class AddTag(val newTag: String): UiEvent
    data class RemoveTag(val tagToRemove: String): UiEvent

    // Deletion
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
    data class OnSortMethodChosen(val sortingMethod: QuizSortMethod): UiEvent
    data class OnSortDirectionChosen(val sortDirection: SortDirection): UiEvent
    data object OnMoveFavoritesToTopToggle: UiEvent
    data object HideSortingSheet: UiEvent

    data class OnArchived(val quiz: Quiz): UiEvent

    // Local Import/Export feature
    data object ExportQuizLocally: UiEvent
    data object ImportQuizLocally: UiEvent
    data object ExportProgressLocally: UiEvent
    data object ImportProgressLocally: UiEvent
    data class ObtainImportedData(val jsonString: String): UiEvent
    data object ShareQuiz: UiEvent

    // Settings & Reset progress
    data object SettingsClicked: UiEvent
    data object ResetClicked: UiEvent // Show Warning dialog first
    data object ImportClicked: UiEvent
    data object ResetProgress: UiEvent

    // About
    data object AboutClicked: UiEvent
}