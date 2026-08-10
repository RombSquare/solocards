package com.rombsquare.solocards.domain.usecases.history

import com.rombsquare.solocards.domain.models.GameResult
import com.rombsquare.solocards.domain.repos.DataRepo

class DeleteGameResult(
    val repo: DataRepo
) {
    suspend operator fun invoke(result: GameResult) {
        repo.deleteGameResult(result)
    }
}