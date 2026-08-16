package com.rombsquare.solocards.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Progress(
    val quizzes: List<Quiz>,
    val cards: List<Card>,
    val gameResults: List<GameResult>
)