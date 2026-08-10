package com.rombsquare.solocards.domain.models

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

data class GameResult(
    val id: Long = 0,
    val quizId: Long,
    val createdAt: Instant = Clock.System.now(),
    val score: Int,
    val satisfaction: Satisfaction,
    val cardCount: Int,
    val gameTime: Duration = (60*4+4).seconds,
    val gameMode: GameMode,
)