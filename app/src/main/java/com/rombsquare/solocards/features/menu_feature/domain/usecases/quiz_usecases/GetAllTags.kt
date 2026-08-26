package com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases

import com.rombsquare.solocards.core.domain.repos.DataRepo
import kotlinx.coroutines.flow.first

class GetAllTags(
    val repo: DataRepo
) {
    suspend operator fun invoke(): List<String> {
        val quizzes = repo
            .getAllQuizzes()
            .first()
            .filter { !it.isTrashed }

        val tags = mutableSetOf<String>()

        quizzes.forEach { quiz ->
            quiz.tags.forEach { tag ->
                tags.add(tag)
            }
        }

        return tags.toList()
    }
}