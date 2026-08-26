package com.rombsquare.solocards.features.menu_feature.data.json

import com.rombsquare.solocards.core.domain.models.Progress
import com.rombsquare.solocards.features.menu_feature.domain.models.QuizWithCards
import com.rombsquare.solocards.features.menu_feature.domain.repos.SerializerRepo

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