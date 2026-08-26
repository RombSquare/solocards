package com.rombsquare.solocards.core.data.room.sessions

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rombsquare.solocards.core.data.room.quizzes.QuizEntity
import com.rombsquare.solocards.core.domain.models.Satisfaction
import kotlin.time.Clock
import kotlin.time.Instant

@Entity(
    tableName = "gameResults",
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
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val resultId: Long = 0,
    val quizId: Long,
    val createdAt: Instant = Clock.System.now(),
    val score: Int,
    val satisfaction: Satisfaction,
    val cardCount: Int,
    val gameTime: Int,
    val gameMode: Int,
)