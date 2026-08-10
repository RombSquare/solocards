package com.rombsquare.solocards.domain.usecases.quizzes

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

class GenerateExamples(
    val repo: DataRepo
) {
    suspend operator fun invoke() {
        repeat(10) { i ->
            val id = repo.insertQuiz(
                Quiz(
                    title = "Test ${i*(i+1)}",
                    tags = listOf("ThisIsTag${i%3}", if (i<5) "FirstHalf" else "SecondHalf")
                )
            )

            repeat(5) { j ->
                repo.insertCard(
                    Card(
                        quizId = id,
                        question = "What is $j * ({a} + {b})?",
                        answer = "{c}",
                        code = "a = rand(1,20)\nb = rand(1,20)\nc = $j*(a+b)",
                        isCodeEnabled = true,
                        count = 1,
                        options = listOf("Red", "Orange", "Green", "Blue", "Yellow")
                    )
                )
            }
        }
    }
}