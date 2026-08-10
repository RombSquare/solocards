package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

class AddTagToQuiz(
    val repo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz, newTag: String) {
        repo.updateQuiz(
            quiz.copy(
                tags = quiz.tags.plus(newTag)
            )
        )
    }
}