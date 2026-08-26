package com.rombsquare.solocards.core.domain.usecases.validation.card_validation

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.CardValidationError
import com.rombsquare.solocards.core.domain.models.ValidationResult
import com.rombsquare.solocards.core.domain.models.GameMode

// There are requirements that must be followed for Mixed Mode:
// * No cards with no allowed modes
// * If there is a card with Boolean Mode allowed, this card must contain at least 1 option
// * If there is a card with Option Mode allowed, this card must contain not less than the option count of this card

class MixedModeValidation {
    operator fun invoke(cards: List<Card>): ValidationResult {
        if (cards.any { it.allowedModes.isEmpty() }) {
            return ValidationResult.Failure(CardValidationError.NoAllowedModes)
        }

        if (
            cards.any { card ->
                card.allowedModes.contains(GameMode.Boolean) && card.options.filterNot { it.isEmpty() }.isEmpty()
            }
        ) {
            return ValidationResult.Failure(CardValidationError.EmptyOptions)
        }

        if (
            cards.any { card ->
                card.allowedModes.contains(GameMode.Option) && (card.options.size+1 < card.optionCount)
            }
        ) {
            return ValidationResult.Failure(CardValidationError.NotEnoughOptions)
        }

        return ValidationResult.Success
    }
}