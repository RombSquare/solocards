package com.rombsquare.solocards.core.domain.usecases.validation.card_validation

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.CardValidationError
import com.rombsquare.solocards.core.domain.models.ValidationResult

// Check if question/answer size are limited

class EditCardValidation {
    operator fun invoke(cards: List<Card>): ValidationResult {
        return if (
            cards.any { card ->
                card.question.length > 50 || card.answer.length > 50
            }
        ) {
            ValidationResult.Failure(CardValidationError.TooBigQuestionAnswer)
        } else {
            ValidationResult.Success
        }
    }
}