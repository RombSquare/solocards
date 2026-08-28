package com.rombsquare.solocards.core.domain.usecases.scripting

import com.rombsquare.solocards.core.domain.models.GeneratingResult
import com.rombsquare.solocards.core.domain.repos.DataRepo
import com.rombsquare.solocards.core.domain.utils.scripting.QuizGenerator
import kotlinx.coroutines.flow.first

// Execute all cards in a quiz
class GenerateQuiz(
    private val quizGenerator: QuizGenerator,
    private val repo: DataRepo
) {
    suspend operator fun invoke(quizId: Long): GeneratingResult {
        val quiz = repo.getQuiz(quizId)!!
        val cards = repo.getCardsByQuiz(quizId).first()

        return quizGenerator.generateQuiz(quiz, cards)
    }
}