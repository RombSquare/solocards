package com.rombsquare.solocards.domain.usecases.serializer

import com.rombsquare.solocards.domain.models.Progress
import com.rombsquare.solocards.domain.models.QuizWithCards
import com.rombsquare.solocards.domain.repos.DataRepo
import com.rombsquare.solocards.domain.repos.SerializerRepo
import kotlinx.coroutines.flow.first

class SerializeProgress(
    val dataRepo: DataRepo,
    val serializerRepo: SerializerRepo
) {
    suspend operator fun invoke(): String {
        val quizzes = dataRepo.getAllQuizzes().first()
        val cards = quizzes.flatMap { dataRepo.getCardsByQuiz(it.id).first() }
        val gameResults = quizzes.flatMap { dataRepo.getGameResultsByQuiz(it.id).first() }

        val progress = Progress(quizzes, cards, gameResults)

        return serializerRepo.serializeProgress(progress)
    }
}