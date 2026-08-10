package com.rombsquare.solocards.domain.models

import kotlin.time.Clock
import kotlin.time.Instant

data class Quiz(
    val id: Long = 0,

    // For Menu Screen
    val title: String = "Untitled",
    val tags: List<String> = emptyList(),
    val isFav: Boolean = false,
    val isTrashed: Boolean = false,
    val isArchived: Boolean = false,

    // Quiz settings
    val showAnswer: Boolean = true,
    val shuffleCards: Boolean = true,
    val swapCardSides: Boolean = false,
    val rateQuiz: Boolean = true,
    val showTreatAsCorrect: Boolean = true,
    val cardCount: Int? = null, // If null, take all cards

    // Date
    val createdAt: Instant = Clock.System.now(),
    val modifiedAt: Instant = Clock.System.now(),
)
