package com.rombsquare.solocards.data.room.game_results

import com.rombsquare.solocards.domain.models.GameMode
import com.rombsquare.solocards.domain.models.GameResult
import kotlin.time.Duration.Companion.seconds

fun GameResultEntity.toDomain() = GameResult(
    this.resultId,
    this.quizId,
    this.createdAt,
    this.score,
    this.satisfaction,
    this.cardCount,
    this.gameTime.seconds,
    GameMode.entries[this.gameMode]
)

fun GameResult.toEntity() = GameResultEntity(
    this.id,
    this.quizId,
    this.createdAt,
    this.score,
    this.satisfaction,
    this.cardCount,
    this.gameTime.inWholeSeconds.toInt(),
    GameMode.entries.indexOf(this.gameMode)
)