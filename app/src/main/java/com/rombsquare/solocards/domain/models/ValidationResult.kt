package com.rombsquare.solocards.domain.models

sealed interface CardValidationResult {
    data object Success: CardValidationResult
    data class Failure(val reason: CardValidationError): CardValidationResult
}

sealed interface CardValidationError {
    data object EmptyQuestionAnswer: CardValidationError
    data object EmptyOptions: CardValidationError
    data object NotEnoughOptions: CardValidationError
    data object NoAllowedModes: CardValidationError
    data object TooBigQuestionAnswer: CardValidationError
}