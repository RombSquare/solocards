package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

class InsertQuiz(
    private val repo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz): Long {
        val quizId = repo.insertQuiz(quiz)
        repo.insertCard(
            Card(
                quizId = quizId,
                question = "",
                answer = "",
            )
        )

        return quizId
    }
}