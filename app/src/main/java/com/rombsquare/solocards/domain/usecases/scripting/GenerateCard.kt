package com.rombsquare.solocards.domain.usecases.scripting

import com.rombsquare.solocards.domain.CardGenerator
import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.GeneratedCard

class GenerateCard(
    private val cardGenerator: CardGenerator
) {
    suspend operator fun invoke(card: Card): Result<GeneratedCard> {
        return cardGenerator.generateCard(card)
    }
}