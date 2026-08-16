package com.rombsquare.solocards.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class QuizWithCards(
    val quiz: Quiz,
    val cards: List<Card>
)