package com.rombsquare.solocards.domain.usecases.cards

import com.rombsquare.solocards.domain.models.GeneratedCard

// Takes a generated card and returns its options
class GenerateOptions {
    operator fun invoke(generatedCard: GeneratedCard): List<String> {
        return generatedCard.options
            .shuffled()
            .take(generatedCard.optionCount-1)
            .plus(generatedCard.answer)
            .shuffled()
    }
}