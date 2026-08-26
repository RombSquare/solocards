package com.rombsquare.solocards.core.domain.usecases.history

import com.rombsquare.solocards.core.domain.models.Session
import com.rombsquare.solocards.core.domain.repos.DataRepo

class InsertGameResult(
    val repo: DataRepo
) {
    suspend operator fun invoke(result: Session): Long {
        return repo.insertGameResult(result)
    }
}