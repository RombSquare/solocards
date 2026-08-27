package com.rombsquare.solocards.core.domain.usecases.validation.tag_validation

import com.rombsquare.solocards.core.domain.models.TagValidationError
import com.rombsquare.solocards.core.domain.models.ValidationResult

class TagValidation {
    operator fun invoke(enteredTag: String, tagListOfQuiz: List<String>): ValidationResult {

        val trimmedTag = enteredTag.trim()

        // Check for length
        if (trimmedTag.length > 15)
            return ValidationResult.Failure(TagValidationError.TagIsTooLong)

        // Check if it doesn't contain space
        if (trimmedTag.contains(" "))
            return ValidationResult.Failure(TagValidationError.WrongTagName)

        // Check if it's not empty
        if (trimmedTag.isEmpty())
            return ValidationResult.Failure(TagValidationError.EmptyTag)

        // Check if this tag already exists
        if (tagListOfQuiz.map { it.lowercase() }.contains(trimmedTag))
            return ValidationResult.Failure(TagValidationError.TagExists)

        // Check if there are too many tags in the list
        if (tagListOfQuiz.size >= 10)
            return ValidationResult.Failure(TagValidationError.TooManyTags)

        return ValidationResult.Success

    }
}