package com.rombsquare.solocards.core.data.room.cards

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rombsquare.solocards.core.data.room.quizzes.QuizEntity

@Entity(
    tableName = "cards",
    foreignKeys = [
        ForeignKey(
            entity = QuizEntity::class,
            parentColumns = ["quizId"],
            childColumns = ["quizId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("quizId")]
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val cardId: Long = 0,
    val quizId: Long,
    val question: String,
    val answer: String,
    val code: String,
    val count: Int,
    val options: String, // Separated by comma
    val optionCount: Int,
    val allowedModes: String, // Contains a string of 4 binary digits
)