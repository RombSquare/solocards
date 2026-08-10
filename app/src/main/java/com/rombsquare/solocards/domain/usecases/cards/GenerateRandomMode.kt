package com.rombsquare.solocards.domain.usecases.cards

import com.rombsquare.solocards.domain.models.GameMode
import com.rombsquare.solocards.domain.models.GeneratedCard

// Generate random mode for Card as long as it's Mixed mode
class GenerateRandomMode {
    operator fun invoke(generatedCard: GeneratedCard): GameMode {
        return generatedCard.allowedModes
            .filter { (_, value) -> value }
            .keys
            .random()
    }
}