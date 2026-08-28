package com.rombsquare.solocards

import com.rombsquare.solocards.core.data.lua.ScriptRepoImpl
import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.domain.models.GeneratingResult
import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.repos.ScriptRepo
import com.rombsquare.solocards.core.domain.utils.scripting.CardGenerator
import com.rombsquare.solocards.core.domain.utils.scripting.QuizGenerator
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import java.lang.Integer.min
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

class ScriptTest {
    private val scriptRepo: ScriptRepo = ScriptRepoImpl()
    private val cardGenerator = CardGenerator(scriptRepo)
    private val quizGenerator = QuizGenerator(cardGenerator)

    // Tests the correctness of generating a single card
    @Test
    fun `generate card`() = runTest {
        val random = Random(1234567)

        val testA = random.nextInt() % 1_000_000
        val testB = random.nextInt() % 1_000_000
        val testTaskType = listOf("parity", "average", "min", "max")[random.nextInt()%4]

        val testC = testA + testB

        val card = Card(
            quizId = 1,
            question = "What is {a} + {b}? Also find the {taskType}",
            answer = "{c}, {taskResult}",
            code = """
                a = $testA
                b = $testB
                c = a+b
                
                taskType = "$testTaskType"
                op1, op2, op3 = randOptions(0,1000,3,c)
                
                op1 = string.format("%s, %s", op1, pick({"parity", "average", "min", "max"}))
                op2 = string.format("%s, %s", op2, pick({"parity", "average", "min", "max"}))
                op3 = string.format("%s, %s", op3, pick({"parity", "average", "min", "max"}))
                
                if taskType == "parity" then
                    if c % 2 == 0 then
                        taskResult = "even"
                    else
                        taskResult = "odd"
                    end
                    
                elseif taskType == "average" then
                    taskResult = c/2
                    
                elseif taskType == min then
                    if a < b then
                        taskResult = a
                    else
                        taskResult = b
                    end
                    
                elseif taskType == max then
                    if a > b then
                        taskResult = a
                    else
                        taskResult = b
                    end
                end
            """.trimIndent(),
            options = listOf("{op1}", "{op2}", "{op3}"),
            allowedModes = GameMode.entries.dropLast(1) // Except Mixed, allowed modes can't contain mixed mode
        )

        cardGenerator.generateCard(card)
            .onSuccess { generatedCard ->
                val testTaskResult = when (testTaskType) {
                    "parity" -> if(testC % 2 == 0) "even" else "odd"
                    "average" -> testC / 2
                    "min" -> min(testA, testB)
                    "max" -> max(testA, testB)
                    else -> fail("Unknown task type (must be parity, average, min or max)")
                }

                // Is the question correct?
                assertEquals(
                    generatedCard.question,
                    "What is $testA + $testB? Also find the $testTaskType"
                )

                // Is the answer correct?
                assertEquals(
                    generatedCard.answer,
                    "$testC, $testTaskResult"
                )

                // If there is a curly brace in option list, it means that options aren't compiled
                if (generatedCard.options.any { it.contains("{") }) {
                    fail("Some option hasn't been generated (because it contains curly brace)")
                }
            }

            .onFailure { e ->
                fail(e.message)
            }
    }

    // Test the correctness of the count of generated cards
    @Test
    fun `check card count of quiz`() = runTest {
        val random = Random(123456)

        val cards = generateCardExamples(40, random)
        val quiz = Quiz(id = 100)

        when (val result = quizGenerator.generateQuiz(quiz, cards)) {
            is GeneratingResult.Success -> {

                // Check the card count
                assertEquals(
                    cards.sumOf { it.count },
                    result.cards.size
                )
            }

            is GeneratingResult.Failure -> {
                fail("Failed to generate a quiz. Card with error: ${result.wrongCard}. Reason: ${result.reason}")
            }
        }
    }

    private fun generateCardExamples(count: Int, random: Random, quizId: Long = 100L): List<Card> {
        val cardList = mutableListOf<Card>()

        repeat(count) { i ->
            cardList.add(Card(
                id = i.toLong(),
                quizId = quizId,
                question = "What is {a} squared?",
                answer = "{b}",
                code = """
                    a = rand(1, 50)
                    b = a*a
                    
                    op1, op2, op3 = randOptions(1, 2500, 3, b)
                """.trimIndent(),
                options = listOf("{op1}", "{op2}", "{op3}"),
                count = abs(random.nextInt()%5 + 1)
            ))
        }

        return cardList
    }
}