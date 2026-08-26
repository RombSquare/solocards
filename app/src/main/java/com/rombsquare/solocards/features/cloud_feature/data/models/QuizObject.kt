package com.rombsquare.solocards.features.cloud_feature.data.models

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@Serializable
@Keep
@IgnoreExtraProperties
data class QuizObject(
    val id: Long = 0,

    // For Menu Screen
    val title: String = "Untitled",
    val tags: List<String> = emptyList(),

    val fav: Boolean = false,
    val trashed: Boolean = false,
    val archived: Boolean = false,

    // Quiz settings
    val showAnswer: Boolean = true,
    val shuffleCards: Boolean = true,
    val swapCardSides: Boolean = false,
    val rateQuiz: Boolean = true,
    val showTreatAsCorrect: Boolean = true,
    val cardCount: Int? = null, // If null, take all cards

    // Date
    val createdAt: Long = 0,
    val modifiedAt: Long = 0,
)