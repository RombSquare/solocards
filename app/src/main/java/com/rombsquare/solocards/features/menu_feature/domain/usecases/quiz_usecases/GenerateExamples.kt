package com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.repos.DataRepo
import kotlinx.coroutines.flow.first

data class QuizExampleUnit(
    val question: String,
    val answer: String,
    val code: String = "",
    val options: List<String> = emptyList(),
    val optionCount: Int = 4,
    val count: Int = 1,
    val allowedModes: List<GameMode> = GameMode.entries
)

val quiz1 = listOf(
    QuizExampleUnit("What's the color of cherry?", "Red"),
    QuizExampleUnit("What's the color of lava?", "Orange"),
    QuizExampleUnit("What's the color of lemon?", "Yellow"),
    QuizExampleUnit("What's the color of grass?", "Grass"),
    QuizExampleUnit("What's the color of sky?", "Cyan"),
    QuizExampleUnit("What's the color of blueberry?", "Blue"),
    QuizExampleUnit("What's the best color for a grape?", "Violet"),
    QuizExampleUnit("What's the color of an empty paper?", "White"),
    QuizExampleUnit("What's the color of a space?", "Black"),
    QuizExampleUnit("What's the color of a thunder cloud?", "Gray"),
    QuizExampleUnit("What's the color of a mirror", "Colorless")
)

val quiz2_experiment = listOf(
    QuizExampleUnit(
        question = "What is {a} + {b}?",
        answer = "{c}",
        code = """
            a = rand(1,10)
            b = rand(1,10)
            c = a + b
            
            option1 = randExcept(1, 5, c)
            option2 = randExcept(6, 10, c)
        """.trimIndent(),
        options = listOf("{option1}", "{option2}"),
        optionCount = 3,
    ),

    QuizExampleUnit(
        question = "How many money do you need to buy {a} apples if you have {b}$ and a single apple costs {c}$?",
        answer = "{d}",
        code = """
            a = rand(2, 9)
            c = rand(1, 9)
            b = rand(c, c*5)
            d = a*c
            
            op1 = randExcept(1, 100, c)
            op2 = randExcept(1, 100, c)
        """.trimIndent(),
        options = listOf("{op1}", "{op2}"),
        optionCount = 3,
    ),

    QuizExampleUnit(
        question = "Is {a} even? (write Y or N)",
        answer = "{b}",
        code = """
            a = rand(100,999)
            if a % 2 == 0 then
                b = 'Y'
                option = 'N'
            else
                b = 'N'
                option = 'Y'
            end
        """.trimIndent(),
        options = listOf("{option}"),
        optionCount = 2,
    ),

    QuizExampleUnit(
        question = "How many eggs if there are {a} dozens of them?",
        answer = "{b}",
        code = """
            a = rand(2,12)
            b = a*12
            
            -- randExcept(minimum, maximum, excluded_number)
            op1 = randExcept(1, 100, b)
            op2 = randExcept(1, 100, b)
            op3 = randExcept(1, 100, b)
        """.trimIndent(),
        options = listOf("{op1}", "{op2}", "{op3}")
    ),

    QuizExampleUnit(
        question = "Find the {type} root of a quadratic equation, if the coefficients are ({a}, {b}, {c})?",
        answer = "{result}",
        code = """
            a = rand(1, 4)
            x1 = rand(1, 10)
            x2 = rand(1, 10)
            b = (-x1 - x2)*a
            c = x1*x2*a
            
            -- The "pick" function returns the random item from the list
            type = pick({"smallest", "biggest"})
            
            if type == "smallest" then
                result = min(x1, x2)
                option = max(x1, x2)
            else
                result = max(x1, x2)
                option = min(x1, x2)
            end          
        """.trimIndent(),
        options = listOf("{option}"),
        optionCount = 2,
    )
)

val quiz2 = listOf(
    QuizExampleUnit(
        question = "What is {a} + {b}?",
        answer = "{c}",
        code = """
            a = rand(1, 20)
            b = rand(1, 20)
            
            c = a+b
            
            -- The function randOptions takes 4 arguments:
            -- Min range (inclusive)
            -- Max range (inclusive)
            -- Number of options
            -- Excluded option (which is the answer)
            
            -- Useful for generating random options
            
            op1, op2, op3 = randOptions(1, 40, 3, c)
        """.trimIndent(),
        count = 5,
        options = listOf("{op1}", "{op2}", "{op3}")
    ),

    QuizExampleUnit(
        question = "What is {a} × {b}?",
        answer = "{c}",
        code = """
            a = rand(1, 10)
            b = rand(1, 10)
            
            c = a*b
            
            op1, op2, op3 = randOptions(1, 100, 3, c)
        """.trimIndent(),
        count = 5,
        options = listOf("{op1}", "{op2}", "{op3}")
    ),

    QuizExampleUnit(
        question = "What is {a} - {b}?",
        answer = "{c}",
        code = """
            a = rand(1, 20)
            b = rand(1, 20)
            
            c = a-b
            
            op1, op2, op3 = randOptions(1, 20, 3, c)
        """.trimIndent(),
        count = 5,
        options = listOf("{op1}", "{op2}", "{op3}")
    ),

    QuizExampleUnit(
        question = "What is {a} + x = {b}",
        answer = "{x}",
        code = """
            a = rand(-10, 10)
            b = rand(-10, 10)
            x = b-a
                        
            op1, op2, op3 = randOptions(-20, 20, 3, x)
        """.trimIndent(),
        count = 5,
        options = listOf("{op1}", "{op2}", "{op3}")
    ),
)

val quiz1_options = quiz1
    .map { it.answer }

class GenerateExamples(
    val repo: DataRepo
) {
    suspend operator fun invoke() {

        // Create these quizzes if the database is empty
        if (repo.getAllQuizzes().first().isNotEmpty()) return

        // Quiz #1

        val exampleQuizId_1 = repo.insertQuiz(
            Quiz(
                title = "Example Quiz",
                tags = listOf("Examples")
            )
        )

        quiz1.forEach { card ->
            repo.insertCard(
                Card(
                    quizId = exampleQuizId_1,
                    question = card.question,
                    answer = card.answer,
                    options = quiz1_options.filter { it != card.answer },
                    allowedModes = GameMode.entries
                )
            )
        }

        val exampleQuizId_2 = repo.insertQuiz(
            Quiz(
                title = "Example Quiz II",
                tags = listOf("Examples")
            )
        )

        quiz2.forEach { card ->
            repo.insertCard(
                Card(
                    quizId = exampleQuizId_2,
                    question = card.question,
                    answer = card.answer,
                    code = card.code,
                    options = card.options,
                    allowedModes = card.allowedModes,
                    optionCount = card.optionCount,
                    count = card.count
                )
            )
        }
    }
}