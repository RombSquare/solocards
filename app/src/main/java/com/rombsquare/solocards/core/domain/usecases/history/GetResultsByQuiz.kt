package com.rombsquare.solocards.core.domain.usecases.history

import com.rombsquare.solocards.core.domain.models.Session
import com.rombsquare.solocards.core.domain.repos.DataRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetResultsByQuiz(
    val repo: DataRepo
) {
    operator fun invoke(quizId: Long): Flow<List<Session>> {
        return repo.getGameResultsByQuiz(quizId).map { gameResults ->
            gameResults.sortedBy { it.createdAt }
        }
    }
}