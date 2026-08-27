package com.rombsquare.solocards.core.domain.models

enum class CardSide {
    Question,
    Answer;

    fun flip(): CardSide {
        return if(this == Question) Answer else Question
    }
}