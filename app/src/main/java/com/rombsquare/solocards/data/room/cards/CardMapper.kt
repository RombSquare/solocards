package com.rombsquare.solocards.data.room.cards

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.GameMode

fun CardEntity.toDomain() = Card(
    this.cardId,
    this.quizId,
    this.question,
    this.answer,
    this.code,
    this.isCodeEnabled,
    this.count,

    options = this.options.split(",").map { it.trim() },

    this.optionCount,

    allowedModes = GameMode.entries
        .dropLast(1) // Drop the Mixed mode
        .zip(this.allowedModes.map { it == '1' })
        .toMap()
)

fun Card.toEntity() = CardEntity(
    this.id,
    this.quizId,
    this.question,
    this.answer,
    this.code,
    this.isCodeEnabled,
    this.count,

    options = this.options.joinToString(","),

    this.optionCount,

    allowedModes = this.allowedModes.values.joinToString(separator = "") { if(it) "1" else "0" }
)