package com.rombsquare.solocards.core.domain.utils.scripting

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.GeneratedCard
import com.rombsquare.solocards.core.domain.repos.ScriptRepo

// Built-in functions for scripting in Solocards
// 1. rand(a, b)  -  generates a random integer in [a; b]
// 2. pick(list)  -  pick a random item from the list
// 3. randOptions(min, max, n, excluded)
// -   it unpacks the values for n variables, where each value is random in [min; max] with excluded value

private val precode = """    
    local rand = math.random
    local min = math.min
    local max = math.max
    local abs = math.abs
    
    local function randExcept(a, b, c)
        local val = math.random(a, b)
        if val == c and a ~= c then
            return a
        end
        return val
    end
    
    local function pick(list)
        if #list == 0 then return nil end
        return list[math.random(#list)]
    end
    
    local function randOptions(min, max, count, excluded)
        local pool = {}
        for i = min, max do
            if i ~= excluded then
                table.insert(pool, i)
            end
        end

        local totalAvailable = #pool
        local selectCount = math.min(count, totalAvailable)

        for i = 1, selectCount do
            local randIndex = math.random(i, totalAvailable)
            pool[i], pool[randIndex] = pool[randIndex], pool[i]
        end

        local results = { table.unpack(pool, 1, selectCount) }
        return table.unpack(results)
    end
""".trimIndent()

// Takes the Card, executes the script, and outputs a GeneratedCard
// It contains ready-made question and answer without variables
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

            if (isBraceMode && symbol.isLetterOrDigit()) {
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

    // It takes a card, runs the card script, and outputs a ready card
    suspend fun generateCard(card: Card): Result<GeneratedCard> {
        val questionVars = getVars(card.question)
        val answerVars = getVars(card.answer)
        val optionVars = getVars(card.options.joinToString())

        val result = scriptRepo.runScript(
            precode = precode, // Built-in utils such as rand(), randOptions() and pick()
            code = card.code,
            vars = questionVars + answerVars + optionVars
        )

        result.onSuccess { vars ->
            // Check for nil variables
            if (vars.containsValue("nil")) {
                val nilVar = vars.keys.find { vars[it] == "nil" }
                return Result.failure(Exception("The card contains the var '${nilVar}' but your code doesn't (or returned nil)"))
            }

            val newQuestion = setVars(card.question, vars)
            val newAnswer = setVars(card.answer, vars)
            val newOptions = card.options.map { setVars(it, vars) }

            return Result.success(
                GeneratedCard(
                    newQuestion,
                    newAnswer,
                    newOptions,
                    card.optionCount,
                    card.allowedModes
                )
            )
        }.onFailure { e ->
            return Result.failure(e)
        }

        throw Exception("Something went wrong during a running code of a card")
    }
}