package com.rombsquare.solocards.domain.usecases.validation.card_validation

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardValidationError
import com.rombsquare.solocards.domain.models.ValidationResult

// Used when user clicks Boolean Mode, but there are no options in this card
// Boolean Mode requires at least one option in incorrect option blank

class BooleanModeValidation {
    operator fun invoke(cards: List<Card>): ValidationResult {
        return if (cards.any { card ->
                card.options
                    .filterNot { it.isEmpty() }
                    .isEmpty()
            }
        ) {
            ValidationResult.Failure(CardValidationError.EmptyOptions)
        } else {
            ValidationResult.Success
        }
    }
}