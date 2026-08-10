package com.rombsquare.solocards.domain.usecases.history

import com.rombsquare.solocards.domain.models.GameResult
import com.rombsquare.solocards.domain.repos.DataRepo

class InsertGameResult(
    val repo: DataRepo
) {
    suspend operator fun invoke(result: GameResult): Long {
        return repo.insertGameResult(result)
    }
}