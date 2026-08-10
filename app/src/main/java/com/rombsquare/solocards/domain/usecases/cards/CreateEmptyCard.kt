package com.rombsquare.solocards.domain.usecases.cards

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.repos.DataRepo

class CreateEmptyCard(
    private val repo: DataRepo
) {
    suspend operator fun invoke(quizId: Long) {
        repo.insertCard(Card(
            quizId = quizId,
            question = "",
            answer = ""
        ))
    }
}