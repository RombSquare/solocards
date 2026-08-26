package com.rombsquare.solocards.core.data.room.sessions

import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.domain.models.Session
import kotlin.time.Duration.Companion.seconds

fun SessionEntity.toDomain() = Session(
    this.resultId,
    this.quizId,
    this.createdAt,
    this.score,
    this.satisfaction,
    this.cardCount,
    this.gameTime.seconds,
    GameMode.entries[this.gameMode]
)

fun Session.toEntity() = SessionEntity(
    this.id,
    this.quizId,
    this.createdAt,
    this.score,
    this.satisfaction,
    this.cardCount,
    this.gameTime.inWholeSeconds.toInt(),
    GameMode.entries.indexOf(this.gameMode)
)