package com.rombsquare.solocards.features.game_feature.domain.usecases.game_cards_usecases

import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.domain.models.GeneratedCard
import kotlin.collections.component1
import kotlin.collections.component2

// Generate random mode for Card as long as it's Mixed mode
class GenerateRandomMode {
    operator fun invoke(generatedCard: GeneratedCard): GameMode {
        return generatedCard.allowedModes.random()
    }
}