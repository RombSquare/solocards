package com.rombsquare.solocards.domain.usecases.serializer

import com.rombsquare.solocards.domain.repos.DataRepo
import com.rombsquare.solocards.domain.repos.SerializerRepo
import kotlinx.serialization.SerializationException

class DeserializeQuiz(
    val dataRepo: DataRepo,
    val serializerRepo: SerializerRepo
) {
    suspend operator fun invoke(jsonString: String) {
        val quizWithCards = serializerRepo.deserializeQuiz(jsonString)

        val newId = dataRepo.insertQuiz(quizWithCards.quiz.copy(id = 0))

        quizWithCards.cards.forEach { card ->
            dataRepo.insertCard(card.copy(id = 0, quizId = newId))
        }
    }
}