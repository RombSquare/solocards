package com.rombsquare.solocards.data.room.game_results

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {

    @Insert
    suspend fun insertGameResult(result: GameResultEntity): Long

    @Delete
    suspend fun deleteGameResult(result: GameResultEntity)

    @Query("SELECT * FROM gameResults WHERE quizId = :quizId")
    fun getByQuiz(quizId: Long): Flow<List<GameResultEntity>>

    @Query("DELETE FROM gameResults WHERE quizId = :quizId")
    suspend fun deleteAllByQuiz(quizId: Long)
}