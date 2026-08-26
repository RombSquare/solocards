package com.rombsquare.solocards.core.domain.utils

import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.repos.DataRepo

class GetQuizById(
    val repo: DataRepo
) {
    suspend operator fun invoke(quizId: Long): Quiz? {
        return repo.getQuiz(quizId)
    }
}