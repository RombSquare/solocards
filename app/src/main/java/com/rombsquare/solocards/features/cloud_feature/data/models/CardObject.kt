package com.rombsquare.solocards.features.cloud_feature.data.models

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.rombsquare.solocards.core.domain.models.GameMode
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
@Keep
@Serializable
data class CardObject(
    val id: Long = 0,
    val quizId: Long = 0,
    val question: String = "",
    val answer: String = "",
    val code: String = "",
    val isCodeEnabled: Boolean = true,
    val count: Int = 1,
    val options: List<String> = emptyList(),
    val optionCount: Int = 4,
    val allowedModes: List<GameMode> = listOf(GameMode.Flip, GameMode.Writing)
)
