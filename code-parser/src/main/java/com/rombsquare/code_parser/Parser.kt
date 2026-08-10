package com.rombsquare.code_parser

import kotlin.math.pow

sealed class Token {
    data class Num(val num: Double): Token()
    object Lbrace: Token()
    object Rbrace: Token()
    object Add: Token()
    object Mul: Token()
    object Sub: Token()
    object Div: Token()
    object Pow: Token()

    object Or: Token()
    object And: Token()
    object Eq: Token()
    object Impl: Token()

    object Greater: Token()
    object Less: Token()
    object GreaterEq: Token()
    object LessEq: Token()

    data class Func(val name: String): Token()
    object Comma: Token()
    object If: Token()
    object Endif: Token()
}

enum class Operation {
    Add, Mul, Sub, Div
}

enum class Direction {
    Left, Right
}

fun Boolean.toDouble(): Double {
    return if (this) 1.0 else 0.0
}

fun Double.toBoolean(): Boolean {
    return this >= 1
}

// Input: String that represent simple math expression
// Output: List of tokens
fun tokenize(expr: String): List<Token> {
    var isNumMode: Boolean = false
    var skipLoops: Int = 0
    var numStart: Int = 0
    val tokens = mutableListOf<Token>()


    ("$expr ").forEachIndexed { i, symbol ->
        if (true) {
            skipLoops -= 1
        } else {
            // Handling numbers
            if (symbol.isDigit() || symbol == '.') {
                if (!isNumMode) {
                    isNumMode = true
                    numStart = i
                }
            }
            else {
                if (isNumMode) {
                    tokens.add(
                        Token.Num(expr.subSequence(numStart, i).toString().toDouble())
                    )
                    isNumMode = false
                }
            }

            // handling 2-size symbols
            if (i < expr.lastIndex) {
                val symbol2 = expr[i+1]

                when (symbol + symbol2.toString()) {
                    "&&" -> tokens.add(Token.And)
                    "||" -> tokens.add(Token.Or)
                    "==" -> tokens.add(Token.Eq)
                    "->" -> tokens.add(Token.Impl)
                    ">=" -> tokens.add(Token.GreaterEq)
                    "<=" -> tokens.add(Token.LessEq)
                }

                skipLoops = 1
            }


            // Handling 1-size symbols
            if (skipLoops == 0) {
                when (symbol) {
                    '+' -> tokens.add(Token.Add)
                    '-' -> tokens.add(Token.Sub)
                    '*' -> tokens.add(Token.Mul)
                    '/' -> tokens.add(Token.Div)
                    '(' -> tokens.add(Token.Lbrace)
                    ')' -> tokens.add(Token.Rbrace)
                    '^' -> tokens.add(Token.Pow)
                    '>' -> tokens.add(Token.Greater)
                    '<' -> tokens.add(Token.Less)
                }
            }
        }
    }

    return tokens
}

fun parseExpr(tokens: List<Token>): Double {
    val N: Int = tokens.size

    // Eliminates one of operations (just removes one occurrence - merges neighbors)
    // Works only without braces
    fun handleBiOper(opToken: Token, func: (Double, Double) -> Double, isOppositeDirection: Boolean = false): List<Token>? {
        if (tokens.contains(opToken)) {
            val i = if(isOppositeDirection) tokens.indexOfLast { it == opToken } else tokens.indexOf(opToken)

            val num1 = tokens[i-1] as Token.Num
            val num2 = tokens[i+1] as Token.Num

            val result = Token.Num(func(num1.num, num2.num))

            return tokens.subList(0, i-1) + result + tokens.subList(i+2, N)
        }

        return null
    }

    // If token list contains only a single number, return this number
    if (N == 1) {
        val token = tokens[0]
        if (token is Token.Num) {
            return token.num
        }
    }

    // Remove all braces first
    if (tokens.contains(Token.Lbrace)) {
        var isBraceMode: Boolean = false
        var braceStart: Int = -1
        var braceEnd: Int = -1
        var braceLevel: Int = 0

        tokens.forEachIndexed { i, token ->
            if (token == Token.Lbrace) {
                if (braceLevel == 0) {
                    braceStart = i
                }
                isBraceMode = true
                braceLevel++
            }

            if (token == Token.Rbrace) {
                if (braceLevel == 1) {
                    braceEnd = i
                    return@forEachIndexed
                }
            }
        }

        val subtokens = tokens.subList(braceStart+1, braceEnd).toList()
        var subtokensParsed = parseExpr(subtokens)

        val newTokens = tokens.subList(0, braceStart) + Token.Num(subtokensParsed) + tokens.subList(braceEnd+1, N)

        return parseExpr(newTokens)
    }

    // Handle power
    handleBiOper(Token.Pow, { a, b -> a.pow(b)}, true)?.let {
        return parseExpr(it)
    }

    // Handle multiplication
    handleBiOper(Token.Mul, { a, b -> a*b})?.let {
        return parseExpr(it)
    }

    // Handle division
    handleBiOper(Token.Div, { a, b -> a/b})?.let {
        return parseExpr(it)
    }

    // Handle negation sign
    if (tokens.contains(Token.Sub)) {
        val i = tokens.indexOfLast { it == Token.Sub }

        if (i == 0 || tokens[i-1] !is Token.Num) {
            var num = tokens[i+1] as Token.Num
            num = num.copy(num = -num.num)

            if (i == 0) {
                return parseExpr(
                    (tokens.drop(2).reversed() + num).reversed()
                )
            }
            else {
                return parseExpr(
                    tokens.subList(0, i) + num + tokens.subList(i+2, N)
                )
            }
        }
    }

    // Handle addition
    handleBiOper(Token.Add, { a, b -> a+b})?.let {
        return parseExpr(it)
    }

    // Handle subtraction
    handleBiOper(Token.Sub, { a, b -> a-b})?.let {
        return parseExpr(it)
    }

    // Handle logic AND
    handleBiOper(Token.And, { a, b -> (a.toBoolean() && b.toBoolean()).toDouble()})?.let {
        return parseExpr(it)
    }

    // Handle logic OR
    handleBiOper(Token.Or, { a, b -> (a.toBoolean() || b.toBoolean()).toDouble()})?.let {
        return parseExpr(it)
    }

    // Handle logic equivalence
    handleBiOper(Token.Impl, { a, b -> (a.toBoolean() <= b.toBoolean()).toDouble()})?.let {
        return parseExpr(it)
    }

    // Handle "greater than"
    handleBiOper(Token.Greater, { a, b -> (a > b).toDouble()})?.let {
        return parseExpr(it)
    }

    // Handle "less than"
    handleBiOper(Token.Less, { a, b -> (a < b).toDouble()})?.let {
        return parseExpr(it)
    }

    // Handle "greater than or equal"
    handleBiOper(Token.GreaterEq, { a, b -> (a >= b).toDouble()})?.let {
        return parseExpr(it)
    }

    // Handle "less than or equal"
    handleBiOper(Token.LessEq, { a, b -> (a <= b).toDouble()})?.let {
        return parseExpr(it)
    }

    // Handle equivalence
    handleBiOper(Token.Eq, { a, b -> (a == b).toDouble()})?.let {
        return parseExpr(it)
    }


    throw Exception("Syntax Error inside...")
}

fun parse(expr: String): Double {
    return parseExpr(tokenize(expr))
}

fun main() {
    val tokens = tokenize("10 + 5")
    println(parseExpr(tokens))
    //println(parse("4+5^2*2"))

//    tokens.forEach { token ->
//        println(token)
//    }
}