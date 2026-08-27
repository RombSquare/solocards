package com.rombsquare.solocards.features.menu_feature.domain.usecases.serializer_usecases

import com.rombsquare.solocards.core.domain.repos.DataRepo
import com.rombsquare.solocards.features.menu_feature.domain.repos.SerializerRepo
import kotlinx.serialization.SerializationException

// If the progress was imported successfully, erase current data and insert new one
class DeserializeProgress(
    val dataRepo: DataRepo,
    val serializerRepo: SerializerRepo
) {
    suspend operator fun invoke(jsonString: String) {
        val progress = serializerRepo.deserializeProgress(jsonString)

        if (progress.quizzes.isEmpty()) {
            throw SerializationException()
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