package com.rombsquare.solocards.core.domain.usecases.validation.card_validation

import com.rombsquare.solocards.core.domain.models.CardValidationError
import com.rombsquare.solocards.core.domain.models.ValidationResult

// Checks if the length of card question/answer it too big
class CardTextValidation {
    operator fun invoke(oldText: String, newText: String): ValidationResult {
        return if (newText < oldText || newText.length < 150) {
            ValidationResult.Success
        } else {
            ValidationResult.Failure(CardValidationError.TooBigQuestionAnswer)
        }
    }
}