package com.rombsquare.solocards.features.cloud_feature.data.mappers

import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.features.cloud_feature.data.models.QuizObject
import kotlin.time.Instant

fun Quiz.toFirestoreObject() = QuizObject(
    this.id,
    this.title,
    this.tags,
    this.isFav,
    this.isTrashed,
    this.isArchived,
    this.showAnswer,
    this.shuffleCards,
    this.swapCardSides,
    this.rateQuiz,
    this.showTreatAsCorrect,
    this.cardCount,
    this.createdAt.toEpochMilliseconds(),
    this.modifiedAt.toEpochMilliseconds()
)

fun QuizObject.toDomain() = Quiz(
    this.id,
    this.title,
    this.tags,
    this.fav,
    this.trashed,
    this.archived,
    this.showAnswer,
    this.shuffleCards,
    this.swapCardSides,
    this.rateQuiz,
    this.showTreatAsCorrect,
    this.cardCount,
    Instant.fromEpochMilliseconds(createdAt),
    Instant.fromEpochMilliseconds(this.modifiedAt)
)