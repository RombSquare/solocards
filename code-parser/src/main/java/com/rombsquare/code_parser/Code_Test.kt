package com.rombsquare.code_parser

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

fun setVars(text: String, vars: Map<String, String>): String {
    var newText = text
    vars.forEach { (name, value) ->
        newText = newText.replace("{$name}", value)
    }
    return newText
}

fun main() {
    val map1 = mapOf<String, String>(
        "X" to "1234",
        "name" to "Vova"
    )
    println(setVars("What is {X} plus {name}?", map1))
}