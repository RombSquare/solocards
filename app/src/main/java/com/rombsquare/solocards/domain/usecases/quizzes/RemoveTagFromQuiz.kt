package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

class RemoveTagFromQuiz(
    val repo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz, tagToRemove: String) {
        repo.updateQuiz(
            quiz.copy(
                tags = quiz.tags.filter { it != tagToRemove  }
            )
        )
    }
}