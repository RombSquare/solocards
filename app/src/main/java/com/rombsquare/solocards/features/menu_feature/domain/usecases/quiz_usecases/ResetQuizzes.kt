package com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases

import com.rombsquare.solocards.core.domain.repos.DataRepo

// Also erases cards inside quizzes
class ResetQuizzes(
    val repo: DataRepo
) {
    suspend operator fun invoke() {
        repo.reset()
    }
}