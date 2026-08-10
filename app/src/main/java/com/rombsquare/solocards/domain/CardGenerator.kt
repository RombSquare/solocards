package com.rombsquare.solocards.domain

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.GeneratedCard
import com.rombsquare.solocards.domain.repos.ScriptRepo

class CardGenerator(
    val scriptRepo: ScriptRepo
) {
    // It outputs the variables that used in a question/answer
    // For example, the vars of "What is {X} + {Y}" are X and Y
    fun getVars(text: String): List<String> {
        val vars = mutableListOf<String>()

        var isBraceMode = false
        var varName = ""

        text.forEach { symbol ->
            if (symbol == '{') {
                isBraceMode = true
            }
            if (symbol == '}') {
                isBraceMode = false
                vars.add(varName)
                varName = ""
            }

            if (isBraceMode && symbol.isLetter()) {
                varName += symbol
            }
        }

        return vars
    }

    // Replace the vars of a string into its values
    // For example: "What is {X} + {Y}" will be "What is 123 + 456"
    // (if X = 123 and Y = 456, the all values are String)
    fun setVars(text: String, vars: Map<String, String>): String {
        var newText = text
        vars.forEach { (name, value) ->
            newText = newText.replace("{$name}", value)
        }
        return newText
    }

    // It takes a card, runs the card script, and outputs a ready card for UI
    suspend fun generateCard(card: Card): Result<GeneratedCard> {
        if (card.isCodeEnabled) {
            val questionVars = getVars(card.question)
            val answerVars = getVars(card.answer)

            val result = scriptRepo.runScript(card.code, questionVars + answerVars)

            result.onSuccess { vars ->
                val newQuestion = setVars(card.question, vars)
                val newAnswer = setVars(card.answer, vars)
                val newOptions = card.options.map { setVars(it, vars) }

                return Result.success(GeneratedCard(newQuestion, newAnswer, newOptions, card.optionCount, card.allowedModes))
            }.onFailure { e ->
                return Result.failure(e)
            }
        } else {

            // If the code is disabled
            return Result.success(GeneratedCard(card.question, card.answer, card.options, card.optionCount, card.allowedModes))
        }

        throw Exception("Something went wrong during a running code of a card")
    }
}