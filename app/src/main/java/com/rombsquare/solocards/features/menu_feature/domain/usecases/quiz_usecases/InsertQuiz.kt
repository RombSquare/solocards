package com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.repos.DataRepo

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