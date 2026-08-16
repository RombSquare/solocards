package com.rombsquare.solocards.data.json

import com.rombsquare.solocards.domain.models.Progress
import com.rombsquare.solocards.domain.models.QuizWithCards
import com.rombsquare.solocards.domain.repos.SerializerRepo

class SerializerRepoImpl: SerializerRepo {
    override fun serializeQuiz(quizWithCards: QuizWithCards): String {
        return JsonSerializer.serializeQuiz(quizWithCards)
    }

    override fun deserializeQuiz(jsonString: String): QuizWithCards {
        return JsonSerializer.deserializeQuiz(jsonString)
    }

    override fun serializeProgress(progress: Progress): String {
        return JsonSerializer.serializeProgress(progress)
    }

    override fun deserializeProgress(jsonString: String): Progress {
        return JsonSerializer.deserializeProgress(jsonString)
    }

}