package com.rombsquare.solocards.domain.models

enum class CardSide {
    Question,
    Answer;

    val isQuestion: Boolean
        get() = this == Question

    val isAnswer: Boolean
        get() = this == Answer

    fun flip(): CardSide {
        return if(this == Question) Answer else Question
    }

    fun reset(): CardSide {
        return Question
    }
}