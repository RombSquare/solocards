package com.rombsquare.solocards.features.cloud_feature.data.models

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.domain.models.Satisfaction
import kotlinx.serialization.Serializable

@Serializable
@Keep
@IgnoreExtraProperties
data class SessionObject(
    val id: Long = 0,
    val quizId: Long = 0,
    val createdAt: Long = 0,
    val score: Int = 0,
    val satisfaction: Satisfaction = Satisfaction.Unknown,
    val cardCount: Int = 0,
    val gameTime: Long = 0,
    val gameMode: GameMode = GameMode.Flip
)