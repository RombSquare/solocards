package com.rombsquare.solocards.domain.usecases.validation.card_validation

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardValidationError
import com.rombsquare.solocards.domain.models.ValidationResult
import com.rombsquare.solocards.domain.models.GameMode

// There are requirements that must be followed for Mixed Mode:
// * No cards with no allowed modes
// * If there is a card with Boolean Mode allowed, this card must contain at least 1 option
// * If there is a card with Option Mode allowed, this card must contain not less than the option count of this card

class MixedModeValidation {
    operator fun invoke(cards: List<Card>): ValidationResult {
        if (cards.any { !it.allowedModes.values.contains(true) }) {
            return ValidationResult.Failure(CardValidationError.NoAllowedModes)
        }

        if (
            cards.any { card ->
                card.allowedModes[GameMode.Boolean] == true && card.options.filterNot { it.isEmpty() }.isEmpty()
            }
        ) {
            return ValidationResult.Failure(CardValidationError.EmptyOptions)
        }

        if (
            cards.any { card ->
                card.allowedModes[GameMode.Option] == true && card.options.size < card.optionCount
            }
        ) {
            return ValidationResult.Failure(CardValidationError.NotEnoughOptions)
        }

        return ValidationResult.Success
    }
}