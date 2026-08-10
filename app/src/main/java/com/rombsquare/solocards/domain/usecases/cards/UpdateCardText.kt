package com.rombsquare.solocards.domain.usecases.cards

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardSide
import com.rombsquare.solocards.domain.repos.DataRepo

class UpdateCardText(
    private val repo: DataRepo
) {
    suspend operator fun invoke(card: Card, newText: String, cardSide: CardSide) {
        when (cardSide) {
            CardSide.Question -> {
                repo.updateCard(
                    card.copy(
                        question = newText
                    )
                )
            }

            CardSide.Answer -> {
                repo.updateCard(
                    card.copy(
                        answer = newText
                    )
                )
            }
        }
    }
}