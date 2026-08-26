package com.rombsquare.solocards.features.editor_feature.domain.usecases.quiz_settings_usecases

import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.repos.DataRepo
import kotlin.time.Clock

class UpdateModifiedDate(
    val repo: DataRepo
) {
    suspend operator fun invoke(quizId: Long) {
        val quiz = repo.getQuiz(quizId)!!
        repo.updateQuiz(quiz.copy(
            modifiedAt = Clock.System.now()
        ))
    }
}