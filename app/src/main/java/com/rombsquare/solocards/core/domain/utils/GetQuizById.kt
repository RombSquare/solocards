package com.rombsquare.solocards.core.domain.utils

import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.repos.DataRepo

// Simple util that helps to get a quiz by its id
class GetQuizById(
    val repo: DataRepo
) {
    suspend operator fun invoke(quizId: Long): Quiz? {
        return repo.getQuiz(quizId)
    }
}