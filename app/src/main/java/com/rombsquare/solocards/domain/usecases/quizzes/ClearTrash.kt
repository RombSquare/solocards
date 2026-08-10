package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

class ClearTrash(
    val repo: DataRepo
) {
    suspend operator fun invoke() {
        repo.clearTrash()
    }
}