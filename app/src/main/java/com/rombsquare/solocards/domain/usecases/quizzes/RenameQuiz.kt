package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

class RenameQuiz(
    val repo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz, newName: String) {
        repo.updateQuiz(quiz.copy(
            title = newName
        ))
    }
}