package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

class UpdateTagsOfQuiz(
    val repo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz, tags: List<String>) {
        repo.updateQuiz(quiz.copy(
            tags = tags
        ))
    }
}