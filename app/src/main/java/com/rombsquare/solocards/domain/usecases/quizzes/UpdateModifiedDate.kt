package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo
import kotlin.time.Clock

class UpdateModifiedDate(
    val repo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz) {
        repo.updateQuiz(quiz.copy(
            modifiedAt = Clock.System.now()
        ))
    }
}