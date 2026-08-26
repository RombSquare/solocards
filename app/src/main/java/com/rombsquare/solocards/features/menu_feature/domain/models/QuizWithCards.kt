package com.rombsquare.solocards.features.menu_feature.domain.models

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.Quiz
import kotlinx.serialization.Serializable

@Serializable
data class QuizWithCards(
    val quiz: Quiz,
    val cards: List<Card>
)