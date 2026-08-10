package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

class UpdateQuiz(
    val repo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz) {
        repo.updateQuiz(quiz)
    }
}