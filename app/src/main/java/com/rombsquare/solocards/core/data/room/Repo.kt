package com.rombsquare.solocards.core.data.room

import com.rombsquare.solocards.core.data.room.cards.CardDao
import com.rombsquare.solocards.core.data.room.cards.toDomain
import com.rombsquare.solocards.core.data.room.cards.toEntity
import com.rombsquare.solocards.core.data.room.sessions.SessionDao
import com.rombsquare.solocards.core.data.room.quizzes.QuizDao
import com.rombsquare.solocards.core.data.room.sessions.toEntity
import com.rombsquare.solocards.core.data.room.sessions.toDomain
import com.rombsquare.solocards.core.data.room.quizzes.toEntity
import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.Session
import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.repos.DataRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.rombsquare.solocards.core.data.room.quizzes.toDomain

class DataRepoImpl(
    private val quizDao: QuizDao,
    private val cardDao: CardDao,
    private val sessionDao: SessionDao,
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

    override fun getGameResultsByQuiz(quizId: Long): Flow<List<Session>> {
        return sessionDao.getByQuiz(quizId).map { gameResults ->
            gameResults.map { it.toDomain() }
        }
    }

    override suspend fun insertGameResult(result: Session): Long {
        return sessionDao.insertGameResult(result.toEntity())
    }

    override suspend fun deleteGameResult(result: Session) {
        sessionDao.deleteGameResult(result.toEntity())
    }

    override suspend fun deleteResultsByQuiz(quizId: Long) {
        sessionDao.deleteAllByQuiz(quizId)
    }
}