package com.rombsquare.solocards.features.cloud_feature.data.mappers

import com.rombsquare.solocards.features.menu_feature.domain.models.Progress
import com.rombsquare.solocards.features.cloud_feature.data.models.ProgressObject

fun Progress.toFirestoreObject() = ProgressObject(
    quizzes = this.quizzes.map { it.toFirestoreObject() },
    cards = this.cards.map { it.toFirestoreObject() },
    sessions = this.sessions.map { it.toFirestoreObject() }
)

fun ProgressObject.toDomain() = Progress(
    quizzes = this.quizzes.map { it.toDomain() },
    cards = this.cards.map { it.toDomain() },
    sessions = this.sessions.map { it.toDomain() }
)