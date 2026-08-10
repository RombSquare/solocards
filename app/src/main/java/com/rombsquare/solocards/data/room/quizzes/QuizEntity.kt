package com.rombsquare.solocards.data.room.quizzes

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.Instant

@Entity(tableName = "quizzes")
data class QuizEntity(
    @PrimaryKey(autoGenerate = true)
    val quizId: Long = 0,

    // For Menu Screen
    val title: String,
    val tags: String,
    val isFav: Boolean,
    val isTrashed: Boolean,
    val isArchived: Boolean,

    // Quiz settings
    val showAnswer: Boolean,
    val shuffleCards: Boolean,
    val swapCardSides: Boolean,
    val rateQuiz: Boolean,
    val showTreatAsCorrect: Boolean,
    val cardCount: Int?, // If null, take all cards

    // Date
    val createdAt: Instant,
    val modifiedAt: Instant,
)