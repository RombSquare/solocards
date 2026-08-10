package com.rombsquare.solocards.domain.repos

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.GameResult
import com.rombsquare.solocards.domain.models.Quiz
import kotlinx.coroutines.flow.Flow

interface DataRepo {

    // ---- Quizzes ----

    suspend fun insertQuiz(quiz: Quiz): Long
    suspend fun getQuiz(quizId: Long): Quiz?
    fun getAllQuizzes(): Flow<List<Quiz>>
    suspend fun deleteQuiz(quiz: Quiz)
    suspend fun updateQuiz(quiz: Quiz)
    suspend fun clearTrash()
    suspend fun reset()

    // ---- Cards ----

    fun getCardsByQuiz(quizId: Long): Flow<List<Card>>
    suspend fun insertCard(card: Card): Long
    suspend fun deleteCard(card: Card)
    suspend fun updateCard(card: Card)

    // ---- GameResults ----

    fun getGameResultsByQuiz(quizId: Long): Flow<List<GameResult>>
    suspend fun insertGameResult(result: GameResult): Long
    suspend fun deleteGameResult(result: GameResult)
    suspend fun deleteResultsByQuiz(quizId: Long)
}