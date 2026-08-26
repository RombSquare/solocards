package com.rombsquare.solocards.core.data.room.sessions

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insertGameResult(result: SessionEntity): Long

    @Delete
    suspend fun deleteGameResult(result: SessionEntity)

    @Query("SELECT * FROM gameResults WHERE quizId = :quizId")
    fun getByQuiz(quizId: Long): Flow<List<SessionEntity>>

    @Query("DELETE FROM gameResults WHERE quizId = :quizId")
    suspend fun deleteAllByQuiz(quizId: Long)
}