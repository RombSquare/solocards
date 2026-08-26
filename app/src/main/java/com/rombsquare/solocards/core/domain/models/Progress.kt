package com.rombsquare.solocards.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Progress(
    val quizzes: List<Quiz> = emptyList(),
    val cards: List<Card> = emptyList(),
    val sessions: List<Session> = emptyList()
)