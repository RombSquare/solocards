package com.rombsquare.solocards.features.cloud_feature.data.mappers

import com.rombsquare.solocards.core.domain.models.Session
import com.rombsquare.solocards.features.cloud_feature.data.models.SessionObject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

fun Session.toFirestoreObject() = SessionObject(
    this.id,
    this.quizId,
    this.createdAt.toEpochMilliseconds(),
    this.score,
    this.satisfaction,
    this.cardCount,
    this.gameTime.inWholeSeconds,
    this.gameMode
)

fun SessionObject.toDomain() = Session(
    this.id,
    this.quizId,
    Instant.fromEpochMilliseconds(this.createdAt),
    this.score,
    this.satisfaction,
    this.cardCount,
    this.gameTime.seconds,
    this.gameMode
)