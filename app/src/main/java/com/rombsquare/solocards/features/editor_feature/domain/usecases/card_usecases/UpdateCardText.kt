package com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.CardSide
import com.rombsquare.solocards.core.domain.repos.DataRepo

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