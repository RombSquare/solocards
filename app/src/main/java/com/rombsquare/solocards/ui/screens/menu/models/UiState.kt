package com.rombsquare.solocards.ui.screens.menu.models

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.models.Section

data class UiState(
    val quizzes: List<Quiz> = emptyList(),
    val dialog: Dialog? = null,
    val selectedQuizId: Long? = null,
    val tags: List<String> = emptyList(),

    // Search
    val searchMode: Boolean = false,
    val searchText: String = "",

    // Sorting
    val showSortingSheet: Boolean = true,
    val sortingMethod: QuizSortingMethod = QuizSortingMethod.ByName,

    // Tags, favs, trash, all
    val section: Section = Section.Everything,
    val topBarTitle: String = "Quizzes",
) {
    val selectedQuiz: Quiz?
        get() = quizzes.find { it.id == selectedQuizId }
}
