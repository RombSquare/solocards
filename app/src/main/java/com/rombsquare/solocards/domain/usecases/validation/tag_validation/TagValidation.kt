package com.rombsquare.solocards.domain.usecases.validation.tag_validation

import com.rombsquare.solocards.domain.models.TagValidationError
import com.rombsquare.solocards.domain.models.ValidationResult

class TagValidation {
    operator fun invoke(enteredTag: String, tagListOfQuiz: List<String>): ValidationResult {

        val trimmedTag = enteredTag.trim()

        if (trimmedTag.length > 15)
            return ValidationResult.Failure(TagValidationError.TagIsTooLong)

        if (trimmedTag.contains(" "))
            return ValidationResult.Failure(TagValidationError.WrongTagName)

        if (trimmedTag.isEmpty())
            return ValidationResult.Failure(TagValidationError.EmptyTag)

        if (tagListOfQuiz.map { it.lowercase() }.contains(trimmedTag))
            return ValidationResult.Failure(TagValidationError.TagExists)

        if (tagListOfQuiz.size >= 10)
            return ValidationResult.Failure(TagValidationError.TooManyTags)

        return ValidationResult.Success

    }
}