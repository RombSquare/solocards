package com.rombsquare.solocards.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rombsquare.solocards.data.room.converters.DurationConverter
import com.rombsquare.solocards.data.room.converters.InstantConverters
import com.rombsquare.solocards.data.room.converters.SatisfactionConverter
import com.rombsquare.solocards.data.room.cards.CardDao
import com.rombsquare.solocards.data.room.game_results.GameResultDao
import com.rombsquare.solocards.data.room.quizzes.QuizDao
import com.rombsquare.solocards.data.room.cards.CardEntity
import com.rombsquare.solocards.data.room.game_results.GameResultEntity
import com.rombsquare.solocards.data.room.quizzes.QuizEntity

@Database(
    entities = [QuizEntity::class, CardEntity::class, GameResultEntity::class],
    version = 22
)
@TypeConverters(InstantConverters::class, SatisfactionConverter::class, DurationConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
    abstract fun cardDao(): CardDao
    abstract fun gameResultDao(): GameResultDao
}