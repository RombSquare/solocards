package com.rombsquare.solocards.features.cloud_feature.domain.usecases.storage_usecases

import com.rombsquare.solocards.features.menu_feature.domain.models.Progress
import com.rombsquare.solocards.core.domain.repos.DataRepo
import com.rombsquare.solocards.features.cloud_feature.domain.repos.CloudStorageRepo
import kotlinx.coroutines.flow.first

class ExportData(
    val dataRepo: DataRepo,
    val storageRepo: CloudStorageRepo
) {
    suspend operator fun invoke() {
        val quizzes = dataRepo.getAllQuizzes().first()
        val cards = quizzes.flatMap { dataRepo.getCardsByQuiz(it.id).first() }
        val gameResults = quizzes.flatMap { dataRepo.getGameResultsByQuiz(it.id).first() }

        val progress = Progress(quizzes, cards, gameResults)

        storageRepo.exportData(progress)
    }
}