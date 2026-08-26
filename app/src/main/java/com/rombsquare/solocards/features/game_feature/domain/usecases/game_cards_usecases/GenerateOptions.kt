package com.rombsquare.solocards.features.game_feature.domain.usecases.game_cards_usecases

import com.rombsquare.solocards.core.domain.models.GeneratedCard

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