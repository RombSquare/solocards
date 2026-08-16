package com.rombsquare.solocards.domain.usecases.serializer

import com.rombsquare.solocards.domain.repos.DataRepo
import com.rombsquare.solocards.domain.repos.SerializerRepo


class DeserializeProgress(
    val dataRepo: DataRepo,
    val serializerRepo: SerializerRepo
) {
    suspend operator fun invoke(jsonString: String) {
        val progress = serializerRepo.deserializeProgress(jsonString)
        dataRepo.reset()

        progress.quizzes.forEach { quiz ->
            dataRepo.insertQuiz(quiz)
        }

        progress.cards.forEach { card ->
            dataRepo.insertCard(card)
        }

        progress.gameResults.forEach { gameResult ->
            dataRepo.insertGameResult(gameResult)
        }

    }
}