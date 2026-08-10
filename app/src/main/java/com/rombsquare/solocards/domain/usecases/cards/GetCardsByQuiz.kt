package com.rombsquare.solocards.domain.usecases.cards

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo
import kotlinx.coroutines.flow.Flow

class GetCardsByQuiz(
    private val repo: DataRepo
) {
    operator fun invoke(quizId: Long): Flow<List<Card>> {
        return repo.getCardsByQuiz(quizId)
    }
}