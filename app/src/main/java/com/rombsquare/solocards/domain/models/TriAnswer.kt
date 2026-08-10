package com.rombsquare.solocards.domain.models

// The answer with 3 states
enum class TriAnswer {
    Yes,
    Maybe,
    No
}

// The answer with 2 states
enum class BooleanAnswer {
    True,
    False;

    val isTrue: Boolean get() = this == True
    val isFalse: Boolean get() = this == False
}