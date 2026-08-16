package com.rombsquare.solocards.domain.usecases.validation.card_validation

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardValidationError
import com.rombsquare.solocards.domain.models.ValidationResult

class CardTextValidation {
    operator fun invoke(oldText: String, newText: String): ValidationResult {
        return if (newText < oldText || newText.length < 150) {
            ValidationResult.Success
        } else {
            ValidationResult.Failure(CardValidationError.TooBigQuestionAnswer)
        }
    }
}