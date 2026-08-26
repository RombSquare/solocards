package com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases

import com.rombsquare.solocards.core.domain.repos.DataRepo

class ClearTrash(
    val repo: DataRepo
) {
    suspend operator fun invoke() {
        repo.clearTrash()
    }
}