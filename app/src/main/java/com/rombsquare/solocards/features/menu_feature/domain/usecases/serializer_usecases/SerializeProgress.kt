package com.rombsquare.solocards.features.menu_feature.domain.usecases.serializer_usecases

import com.rombsquare.solocards.core.domain.models.Progress
import com.rombsquare.solocards.core.domain.repos.DataRepo
import com.rombsquare.solocards.features.menu_feature.domain.repos.SerializerRepo
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