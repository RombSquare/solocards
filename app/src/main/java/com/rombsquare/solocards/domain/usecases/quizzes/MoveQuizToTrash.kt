package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

class MoveQuizToTrash(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz) {
        dataRepo.updateQuiz(
            quiz.copy(
                isTrashed = true
            )
        )
    }
}