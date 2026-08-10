package com.rombsquare.solocards.data.room.cards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert
    suspend fun insertCard(card: CardEntity): Long

    @Query("SELECT * FROM cards WHERE quizId = :quizId")
    fun getCardsByQuiz(quizId: Long): Flow<List<CardEntity>>

    @Delete
    suspend fun deleteCard(card: CardEntity)

    @Update
    suspend fun updateCard(card: CardEntity)
}