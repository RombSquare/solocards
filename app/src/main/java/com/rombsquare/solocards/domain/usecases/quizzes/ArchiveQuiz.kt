package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

// This use case archives the quiz
// If the quiz is already archives, it just unarchives it

class ArchiveQuiz(
    val repo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz) {
        repo.updateQuiz(
            quiz.copy(
                isArchived = !quiz.isArchived
            )
        )
    }
}