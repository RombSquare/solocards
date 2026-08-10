package com.rombsquare.solocards.domain.usecases.history

import com.rombsquare.solocards.domain.repos.DataRepo

class DeleteAllByQuiz(
    val repo: DataRepo
) {
    suspend operator fun invoke(quizId: Long) {
        repo.deleteResultsByQuiz(quizId)
    }
}