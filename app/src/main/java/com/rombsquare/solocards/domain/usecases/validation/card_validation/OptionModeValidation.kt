package com.rombsquare.solocards.domain.usecases.validation.card_validation

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardValidationError
import com.rombsquare.solocards.domain.models.ValidationResult

// Used when user clicks Option Mode, but some card has not enough options in option blank

class OptionModeValidation {
    operator fun invoke(cards: List<Card>): ValidationResult {
        return if (
            cards.any { card ->
                card.options.size < card.optionCount
            }
        ) {
            ValidationResult.Failure(CardValidationError.NotEnoughOptions)
        } else {
            ValidationResult.Success
        }
    }
}