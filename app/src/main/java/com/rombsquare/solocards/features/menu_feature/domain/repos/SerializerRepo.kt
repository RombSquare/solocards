package com.rombsquare.solocards.features.menu_feature.domain.repos

import com.rombsquare.solocards.features.menu_feature.domain.models.Progress
import com.rombsquare.solocards.features.menu_feature.domain.models.QuizWithCards

interface SerializerRepo {
    fun serializeQuiz(quizWithCards: QuizWithCards): String
    fun deserializeQuiz(jsonString: String): QuizWithCards

    fun serializeProgress(progress: Progress): String
    fun deserializeProgress(jsonString: String): Progress
}