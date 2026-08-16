package com.rombsquare.solocards.data.json

import com.rombsquare.solocards.domain.models.Progress
import com.rombsquare.solocards.domain.models.QuizWithCards
import kotlinx.serialization.json.Json

object JsonSerializer {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    fun serializeQuiz(quizWithCards: QuizWithCards): String {
        return json.encodeToString(quizWithCards)
    }

    fun deserializeQuiz(jsonString: String): QuizWithCards {
        return json.decodeFromString(jsonString)
    }

    fun serializeProgress(progress: Progress): String {
        return json.encodeToString(progress)
    }

    fun deserializeProgress(jsonString: String): Progress {
        return json.decodeFromString(jsonString)
    }
}