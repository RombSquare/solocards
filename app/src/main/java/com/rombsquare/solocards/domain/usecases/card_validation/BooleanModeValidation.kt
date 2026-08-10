package com.rombsquare.solocards.domain.usecases.card_validation

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardValidationError
import com.rombsquare.solocards.domain.models.CardValidationResult

// Used when user clicks Boolean Mode, but there are no options in this card
// Boolean Mode requires at least one option in incorrect option blank

class BooleanModeValidation {
    operator fun invoke(cards: List<Card>): CardValidationResult {
        return if (cards.any { card ->
                card.options
                    .filterNot { it.isEmpty() }
                    .isEmpty()
            }
        ) {
            CardValidationResult.Failure(CardValidationError.EmptyOptions)
        } else {
            CardValidationResult.Success
        }
    }
}