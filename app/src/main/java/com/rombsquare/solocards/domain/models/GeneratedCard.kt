package com.rombsquare.solocards.domain.models

import androidx.compose.runtime.mutableStateMapOf

// The card, which is ready for being shown in UI
data class GeneratedCard(
    val question: String,
    val answer: String,
    val options: List<String>,
    val optionCount: Int,
    val allowedModes: Map<GameMode, Boolean>
) {

    // Swap question and answer
    fun invert(): GeneratedCard {
        return this.copy(
            question = answer,
            answer = question
        )
    }
}