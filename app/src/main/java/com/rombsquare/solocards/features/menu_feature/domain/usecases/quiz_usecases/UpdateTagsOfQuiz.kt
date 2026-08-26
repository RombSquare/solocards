package com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases

import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.repos.DataRepo

class UpdateTagsOfQuiz(
    val repo: DataRepo
) {
    suspend operator fun invoke(quiz: Quiz, tags: List<String>) {
        repo.updateQuiz(quiz.copy(
            tags = tags
        ))
    }
}