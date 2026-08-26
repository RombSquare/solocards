package com.rombsquare.solocards.core.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rombsquare.solocards.core.data.room.converters.DurationConverter
import com.rombsquare.solocards.core.data.room.converters.InstantConverters
import com.rombsquare.solocards.core.data.room.converters.SatisfactionConverter
import com.rombsquare.solocards.core.data.room.cards.CardDao
import com.rombsquare.solocards.core.data.room.sessions.SessionDao
import com.rombsquare.solocards.core.data.room.quizzes.QuizDao
import com.rombsquare.solocards.core.data.room.cards.CardEntity
import com.rombsquare.solocards.core.data.room.sessions.SessionEntity
import com.rombsquare.solocards.core.data.room.quizzes.QuizEntity

@Database(
    entities = [QuizEntity::class, CardEntity::class, SessionEntity::class],
    version = 23
)
@TypeConverters(InstantConverters::class, SatisfactionConverter::class, DurationConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
    abstract fun cardDao(): CardDao
    abstract fun gameResultDao(): SessionDao
}