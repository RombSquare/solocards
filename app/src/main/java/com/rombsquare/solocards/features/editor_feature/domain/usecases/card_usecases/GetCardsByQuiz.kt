package com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.repos.DataRepo
import kotlinx.coroutines.flow.Flow

class GetCardsByQuiz(
    private val repo: DataRepo
) {
    operator fun invoke(quizId: Long): Flow<List<Card>> {
        return repo.getCardsByQuiz(quizId)
    }
}