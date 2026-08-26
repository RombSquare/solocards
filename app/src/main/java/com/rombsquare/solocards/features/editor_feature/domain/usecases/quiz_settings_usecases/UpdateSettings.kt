package com.rombsquare.solocards.features.editor_feature.domain.usecases.quiz_settings_usecases

import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.repos.DataRepo

class UpdateSettings(
    val repo: DataRepo
) {
    suspend operator fun invoke(
        quizId: Long,
        showAnswer: Boolean,
        shuffleCards: Boolean,
        swapCardSides: Boolean,
        rateQuiz: Boolean,
        showTreatAsCorrect: Boolean,
        cardCount: Int?
    ) {
        val quiz = repo.getQuiz(quizId)!!
        repo.updateQuiz(quiz.copy(
            showAnswer = showAnswer,
            shuffleCards = shuffleCards,
            swapCardSides = swapCardSides,
            rateQuiz = rateQuiz,
            showTreatAsCorrect = showTreatAsCorrect,
            cardCount = cardCount
        ))
    }
}