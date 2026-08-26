package com.rombsquare.solocards.core.domain.models

// The card, which is ready for being shown in UI
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