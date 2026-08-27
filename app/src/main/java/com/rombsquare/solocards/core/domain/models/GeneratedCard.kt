package com.rombsquare.solocards.core.domain.models

// The card with an executed script
data class GeneratedCard(
    val question: String,
    val answer: String,
    val options: List<String>,
    val optionCount: Int,
    val allowedModes: List<GameMode>
) {

    // Swap question and answer
    fun invert(): GeneratedCard {
        return this.copy(
            question = answer,
            answer = question
        )
    }
}