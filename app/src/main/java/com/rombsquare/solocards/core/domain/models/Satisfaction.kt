package com.rombsquare.solocards.core.domain.models

// The user rating at the quiz end
enum class Satisfaction(val string: String) {
    Awful("Awful"),
    Unsatisfied("Not bad"),
    Normal("Normal"),
    Good("Good"),
    Perfect("Perfect"),
    Unknown("Unknown")
}