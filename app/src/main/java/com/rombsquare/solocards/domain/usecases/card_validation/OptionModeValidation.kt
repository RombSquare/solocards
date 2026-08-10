package com.rombsquare.solocards.domain.usecases.card_validation

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardValidationError
import com.rombsquare.solocards.domain.models.CardValidationResult

// Used when user clicks Option Mode, but some card has not enough options in option blank

class OptionModeValidation {
    operator fun invoke(cards: List<Card>): CardValidationResult {
        return if (
            cards.any { card ->
                card.options.size < card.optionCount
            }
        ) {
            CardValidationResult.Failure(CardValidationError.NotEnoughOptions)
        } else {
            CardValidationResult.Success
        }
    }
}