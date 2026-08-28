package com.rombsquare.solocards.features.cloud_feature.data.mappers

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.features.cloud_feature.data.models.CardObject

fun Card.toFirestoreObject() = CardObject(
    this.id,
    this.quizId,
    this.question,
    this.answer,
    this.code,
    this.count,
    this.options,
    this.optionCount,
    this.allowedModes
)

fun CardObject.toDomain() = Card(
    this.id,
    this.quizId,
    this.question,
    this.answer,
    this.code,
    this.count,
    this.options,
    this.optionCount,
    this.allowedModes
)