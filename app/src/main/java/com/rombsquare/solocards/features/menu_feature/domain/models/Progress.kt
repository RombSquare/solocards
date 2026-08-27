package com.rombsquare.solocards.features.menu_feature.domain.models

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.models.Session
import kotlinx.serialization.Serializable

@Serializable
data class Progress(
    val quizzes: List<Quiz> = emptyList(),
    val cards: List<Card> = emptyList(),
    val sessions: List<Session> = emptyList()
)