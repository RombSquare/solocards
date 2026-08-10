package com.rombsquare.solocards.domain.usecases.card_validation

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardValidationError
import com.rombsquare.solocards.domain.models.CardValidationResult

class CardTextValidation {
    operator fun invoke(oldText: String, newText: String): CardValidationResult {
        return if (newText < oldText || newText.length < 150) {
            CardValidationResult.Success
        } else {
            CardValidationResult.Failure(CardValidationError.TooBigQuestionAnswer)
        }
    }
}