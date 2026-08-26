package com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases

import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.repos.DataRepo

class RenameQuiz(
    val repo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz, newName: String) {
        repo.updateQuiz(quiz.copy(
            title = newName
        ))
    }
}