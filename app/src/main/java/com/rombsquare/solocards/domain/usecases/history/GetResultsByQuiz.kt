package com.rombsquare.solocards.domain.usecases.history

import com.rombsquare.solocards.domain.models.GameResult
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetResultsByQuiz(
    val repo: DataRepo
) {
    operator fun invoke(quizId: Long): Flow<List<GameResult>> {
        return repo.getGameResultsByQuiz(quizId).map { gameResults ->
            gameResults.sortedBy { it.createdAt }
        }
    }
}