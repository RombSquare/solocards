package com.rombsquare.solocards.domain.usecases.serializer

import com.rombsquare.solocards.domain.models.QuizWithCards
import com.rombsquare.solocards.domain.repos.DataRepo
import com.rombsquare.solocards.domain.repos.SerializerRepo
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