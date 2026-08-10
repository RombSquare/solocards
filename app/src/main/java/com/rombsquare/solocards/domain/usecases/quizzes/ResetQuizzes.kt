package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.repos.DataRepo

// Also erases cards inside quizzes
class ResetQuizzes(
    val repo: DataRepo
) {
    suspend operator fun invoke() {
        repo.reset()
    }
}