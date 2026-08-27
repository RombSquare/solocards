package com.rombsquare.solocards.core.domain.usecases.validation.quiz_validation

import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.models.QuizValidationError
import com.rombsquare.solocards.core.domain.models.ValidationResult

class QuizValidation {
    operator fun invoke(enteredQuizName: String, quizList: List<Quiz>): ValidationResult {
        val trimmedName = enteredQuizName.trim()

        // Check if there are no other quiz with the same name
        if (quizList.any { it.title.trim() == trimmedName })
            return ValidationResult.Failure(QuizValidationError.NameExists)

        // Check for length
        if (trimmedName.length > 30)
            return ValidationResult.Failure(QuizValidationError.NameIsTooLong)

        // Check if it's not empty
        if (trimmedName.isEmpty())
            return ValidationResult.Failure(QuizValidationError.NameIsEmpty)

        return ValidationResult.Success
    }
}