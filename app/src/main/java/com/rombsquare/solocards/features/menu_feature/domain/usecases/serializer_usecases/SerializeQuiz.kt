package com.rombsquare.solocards.features.menu_feature.domain.usecases.serializer_usecases

import com.rombsquare.solocards.features.menu_feature.domain.models.QuizWithCards
import com.rombsquare.solocards.core.domain.repos.DataRepo
import com.rombsquare.solocards.features.menu_feature.domain.repos.SerializerRepo
import kotlinx.coroutines.flow.first

class SerializeQuiz(
    val dataRepo: DataRepo,
    val serializerRepo: SerializerRepo
) {
    suspend operator fun invoke(quizId: Long): String {
        val quiz = dataRepo.getQuiz(quizId)!!
        val cards = dataRepo.getCardsByQuiz(quizId).first()

        val quizWithCards = QuizWithCards(quiz, cards)

        return serializerRepo.serializeQuiz(quizWithCards)
    }
}