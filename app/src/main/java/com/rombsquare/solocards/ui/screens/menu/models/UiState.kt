package com.rombsquare.solocards.ui.screens.menu.models

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.models.QuizSortMethod
import com.rombsquare.solocards.domain.models.QuizSortOptions
import com.rombsquare.solocards.domain.models.Section
import com.rombsquare.solocards.domain.models.SortDirection

enum class SerializationType {
    Quiz,
    Progress
}

data class UiState(
    val quizzes: List<Quiz> = emptyList(),
    val dialog: Dialog? = null,
    val selectedQuizId: Long? = null,
    val tags: List<String> = emptyList(),

    // Search
    val searchMode: Boolean = false,
    val searchText: String = "",

    // Sorting
    val showSortingSheet: Boolean = false,
    val sortOptions: QuizSortOptions = QuizSortOptions(
        method = QuizSortMethod.ByName,
        direction = SortDirection.Ascending,
        moveFavoritesToTop = true
    ),

    // Tags, favs, trash, all
    val section: Section = Section.Everything,
    val topBarTitle: String = "Quizzes",

    // Toast message
    val toastMessage: String = "",

    // Local Import/Export feature
    val serializedData: String = "",
    val serializationType: SerializationType = SerializationType.Quiz
) {
    val selectedQuiz: Quiz?
        get() = quizzes.find { it.id == selectedQuizId }
}
