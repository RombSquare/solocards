package com.rombsquare.solocards.core.domain.models

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@Serializable
data class Session(
    val id: Long = 0,
    val quizId: Long = 0,
    val createdAt: Instant = Clock.System.now(),
    val score: Int = 0,
    val satisfaction: Satisfaction = Satisfaction.Unknown,
    val cardCount: Int = 0,
    val gameTime: Duration = 0.seconds,
    val gameMode: GameMode = GameMode.Flip,
)