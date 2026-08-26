package com.rombsquare.solocards.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val id: Long = 0,
    val quizId: Long = 0,
    val question: String = "",
    val answer: String = "",
    val code: String = "",
    val isCodeEnabled: Boolean = true,
    val count: Int = 1,
    val options: List<String> = emptyList(),
    val optionCount: Int = 4,
    val allowedModes: List<GameMode> = listOf(GameMode.Flip, GameMode.Writing)
)
