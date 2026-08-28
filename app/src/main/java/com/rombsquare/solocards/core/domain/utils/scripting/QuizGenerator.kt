package com.rombsquare.solocards.core.domain.utils.scripting

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.GeneratedCard
import com.rombsquare.solocards.core.domain.models.GeneratingResult
import com.rombsquare.solocards.core.domain.models.Quiz

// It takes the quiz and list of cards, runs the script on every card and returns the list of generated cards

class QuizGenerator(
    val cardGenerator: CardGenerator
) {
    suspend fun generateQuiz(quiz: Quiz, cards: List<Card>): GeneratingResult {
        var generatedCards = mutableListOf<GeneratedCard>()

        cards.forEach { card ->

            if (card.question.isEmpty() || card.answer.isEmpty()) {
                return GeneratingResult.Failure("The question/answer is empty", card)
            }

            repeat(card.count) {
                val result = cardGenerator.generateCard(card)

                result.onSuccess { generatedCard ->

                    // If swapCardSide param is true, swap question and answer
                    generatedCards.add(
                        generatedCard.invert().takeIf { quiz.swapCardSides } ?: generatedCard
                    )
                }.onFailure { e ->
                    return GeneratingResult.Failure(e.message ?: "Unknown reason", card)
                }
            }
        }

        // If shuffleCards param is true
        if (quiz.shuffleCards) generatedCards.shuffle()

        // If card count is specified
        if (quiz.cardCount != null) generatedCards = generatedCards
            .take(quiz.cardCount)
            .toMutableList()

        return GeneratingResult.Success(generatedCards)
    }
}