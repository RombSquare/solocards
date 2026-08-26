package com.rombsquare.solocards.core.domain.usecases.scripting

import com.rombsquare.solocards.core.domain.utils.CardGenerator
import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.GeneratedCard

class GenerateCard(
    private val cardGenerator: CardGenerator
) {
    suspend operator fun invoke(card: Card): Result<GeneratedCard> {
        return cardGenerator.generateCard(card)
    }
}