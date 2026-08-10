package com.rombsquare.solocards.domain.usecases.scripting

import com.rombsquare.solocards.domain.CardGenerator
import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.GeneratedCard
import com.rombsquare.solocards.domain.models.GeneratingResult
import com.rombsquare.solocards.domain.models.Quiz

class GenerateQuiz(
    private val cardGenerator: CardGenerator,
) {
    suspend operator fun invoke(quiz: Quiz, cards: List<Card>): GeneratingResult {
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