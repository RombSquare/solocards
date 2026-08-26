package com.rombsquare.solocards.features.cloud_feature.domain.usecases.storage_usecases

import com.rombsquare.solocards.core.domain.repos.DataRepo
import com.rombsquare.solocards.features.cloud_feature.domain.repos.CloudStorageRepo
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ImportData(
    val dataRepo: DataRepo,
    val storageRepo: CloudStorageRepo
) {
    suspend operator fun invoke() {
        val progress = suspendCancellableCoroutine { continuation ->
            storageRepo.importData { result ->
                continuation.resume(result)
            }
        }

        if (progress == null) {
            throw Exception("Cannot import data")
        }

        dataRepo.reset()

        progress.quizzes.forEach { quiz ->
            dataRepo.insertQuiz(quiz)
        }

        progress.cards.forEach { card ->
            dataRepo.insertCard(card)
        }

        progress.sessions.forEach { gameResult ->
            dataRepo.insertGameResult(gameResult)
        }
    }
}