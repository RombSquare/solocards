package com.rombsquare.solocards.domain.models

sealed interface ValidationResult {
    data object Success: ValidationResult
    data class Failure(val reason: ValidationError): ValidationResult
}

interface ValidationError

enum class CardValidationError: ValidationError {
    EmptyQuestionAnswer,
    EmptyOptions,
    NotEnoughOptions,
    NoAllowedModes,
    TooBigQuestionAnswer,
}

enum class TagValidationError: ValidationError {
    TagIsTooLong,
    EmptyTag,
    TagExists,
    TooManyTags,
    WrongTagName
}

enum class QuizValidationError: ValidationError {
    NameIsEmpty,
    NameIsTooLong,
    NameExists
}