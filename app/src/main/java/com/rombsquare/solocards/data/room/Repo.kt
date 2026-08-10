package com.rombsquare.solocards.data.room

import com.rombsquare.solocards.data.room.cards.CardDao
import com.rombsquare.solocards.data.room.cards.toDomain
import com.rombsquare.solocards.data.room.cards.toEntity
import com.rombsquare.solocards.data.room.game_results.GameResultDao
import com.rombsquare.solocards.data.room.quizzes.QuizDao
import com.rombsquare.solocards.data.room.game_results.toEntity
import com.rombsquare.solocards.data.room.game_results.toDomain
import com.rombsquare.solocards.data.room.quizzes.toEntity
import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.GameResult
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.rombsquare.solocards.data.room.quizzes.toDomain

class DataRepoImpl(
    private val quizDao: QuizDao,
    private val cardDao: CardDao,
    private val gameResultDao: GameResultDao,
): DataRepo {

    // ---- Quizzes ----

    override suspend fun insertQuiz(quiz: Quiz): Long {
        return quizDao.insertQuiz(quiz.toEntity())
    }

    override suspend fun getQuiz(quizId: Long): Quiz? {
        return quizDao.getQuiz(quizId)?.toDomain()
    }

    override fun getAllQuizzes(): Flow<List<Quiz>> {
        return quizDao.getAllQuizzes().map { quizList ->
            quizList.map { it.toDomain() }
        }
    }

    override suspend fun deleteQuiz(quiz: Quiz) {
        quizDao.deleteQuiz(quiz.toEntity())
    }

    override suspend fun updateQuiz(quiz: Quiz) {
        quizDao.updateQuiz(quiz.toEntity())
    }

    override fun getCardsByQuiz(quizId: Long): Flow<List<Card>> {
        return cardDao.getCardsByQuiz(quizId).map { cardList ->
            cardList.map { it.toDomain() }
        }
    }

    override suspend fun clearTrash() {
        quizDao.clearTrash()
    }

    override suspend fun reset() {
        quizDao.deleteAllQuizzes()
    }

    // ---- Cards -----

    override suspend fun insertCard(card: Card): Long {
        return cardDao.insertCard(card.toEntity())
    }

    override suspend fun deleteCard(card: Card) {
        cardDao.deleteCard(card.toEntity())
    }

    override suspend fun updateCard(card: Card) {
        cardDao.updateCard(card.toEntity())
    }

    // ---- GameResults ----

    override fun getGameResultsByQuiz(quizId: Long): Flow<List<GameResult>> {
        return gameResultDao.getByQuiz(quizId).map { gameResults ->
            gameResults.map { it.toDomain() }
        }
    }

    override suspend fun insertGameResult(result: GameResult): Long {
        return gameResultDao.insertGameResult(result.toEntity())
    }

    override suspend fun deleteGameResult(result: GameResult) {
        gameResultDao.deleteGameResult(result.toEntity())
    }

    override suspend fun deleteResultsByQuiz(quizId: Long) {
        gameResultDao.deleteAllByQuiz(quizId)
    }
}