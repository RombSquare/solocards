package com.rombsquare.solocards.features.game_feature.ui.models

import com.rombsquare.solocards.features.game_feature.domain.models.BooleanAnswer
import com.rombsquare.solocards.features.game_feature.domain.models.TriAnswer

// Types of user answer
sealed interface UserAnswer {
    data class YesMaybeNo(val value: TriAnswer): UserAnswer
    data class Text(val value: String): UserAnswer // Used in Writing and Option mode
    data class TrueFalse(val value: BooleanAnswer): UserAnswer
}