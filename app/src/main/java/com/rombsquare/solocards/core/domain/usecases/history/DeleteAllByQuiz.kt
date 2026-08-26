package com.rombsquare.solocards.core.domain.usecases.history

import com.rombsquare.solocards.core.domain.repos.DataRepo

class DeleteAllByQuiz(
    val repo: DataRepo
) {
    suspend operator fun invoke(quizId: Long) {
        repo.deleteResultsByQuiz(quizId)
    }
}