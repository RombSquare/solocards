package com.rombsquare.solocards.domain.usecases.card_validation

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardValidationError
import com.rombsquare.solocards.domain.models.CardValidationResult

// Check if question/answer size are limited

class EditCardValidation {
    operator fun invoke(cards: List<Card>): CardValidationResult {
        return if (
            cards.any { card ->
                card.question.length > 50 || card.answer.length > 50
            }
        ) {
            CardValidationResult.Failure(CardValidationError.TooBigQuestionAnswer)
        } else {
            CardValidationResult.Success
        }
    }
}