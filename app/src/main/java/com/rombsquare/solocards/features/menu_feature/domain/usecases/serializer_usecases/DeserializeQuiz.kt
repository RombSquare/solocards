package com.rombsquare.solocards.features.menu_feature.domain.usecases.serializer_usecases

import com.rombsquare.solocards.core.domain.repos.DataRepo
import com.rombsquare.solocards.features.menu_feature.domain.repos.SerializerRepo

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