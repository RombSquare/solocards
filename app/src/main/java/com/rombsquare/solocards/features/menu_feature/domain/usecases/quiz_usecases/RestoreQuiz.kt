package com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases

import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.repos.DataRepo

class RestoreQuiz(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz) {
        dataRepo.updateQuiz(
            quiz.copy(
                isTrashed = false
            )
        )
    }
}