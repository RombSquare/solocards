package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo
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