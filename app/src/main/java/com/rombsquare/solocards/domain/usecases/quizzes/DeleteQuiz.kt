package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

class DeleteQuiz(
    val repo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz) {
        repo.deleteQuiz(quiz)
    }
}