package com.rombsquare.solocards.features.cloud_feature.data.models

import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@Serializable
@IgnoreExtraProperties
data class ProgressObject(
    val quizzes: List<QuizObject> = emptyList(),
    val cards: List<CardObject> = emptyList(),
    val sessions: List<SessionObject> = emptyList()
)