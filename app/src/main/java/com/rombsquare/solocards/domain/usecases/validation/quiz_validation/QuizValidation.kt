package com.rombsquare.solocards.domain.usecases.validation.quiz_validation

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.models.QuizValidationError
import com.rombsquare.solocards.domain.models.ValidationResult

class QuizValidation {
    operator fun invoke(enteredQuizName: String, quizList: List<Quiz>): ValidationResult {
        val trimmedName = enteredQuizName.trim()
        if (quizList.any { it.title.trim() == trimmedName })
            return ValidationResult.Failure(QuizValidationError.NameExists)

        if (trimmedName.length > 15)
            return ValidationResult.Failure(QuizValidationError.NameIsTooLong)

        if (trimmedName.isEmpty())
            return ValidationResult.Failure(QuizValidationError.NameIsEmpty)

        return ValidationResult.Success
    }
}