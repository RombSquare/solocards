package com.rombsquare.solocards.core.data.room.cards

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.GameMode

fun CardEntity.toDomain() = Card(
    this.cardId,
    this.quizId,
    this.question,
    this.answer,
    this.code,
    this.count,

    options = this.options.split(",").map { it.trim() },

    this.optionCount,

    allowedModes = if (this.allowedModes.isEmpty()) emptyList() else this.allowedModes
        .split(",")
        .map { GameMode.entries[it.toInt()] }
)

fun Card.toEntity() = CardEntity(
    this.id,
    this.quizId,
    this.question,
    this.answer,
    this.code,
    this.count,

    options = this.options.joinToString(","),

    this.optionCount,

    allowedModes = this.allowedModes
        .joinToString(separator = ",") { it.ordinal.toString() }
)