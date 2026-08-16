package com.rombsquare.solocards.domain.usecases.validation.card_validation

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardValidationError
import com.rombsquare.solocards.domain.models.ValidationResult

// This validation executes when clicked play button at the Editor
// This check happens even before showing the list of modes
// These aspect are checked:
// * Question & Answer must be non-empty

class BasicCardValidation {
    operator fun invoke(cards: List<Card>): ValidationResult {
        return if (
            cards.any { it.question.isEmpty() } || cards.any { it.answer.isEmpty() }
        ) {
            ValidationResult.Failure(CardValidationError.EmptyQuestionAnswer)
        } else {
            ValidationResult.Success
        }
    }
}