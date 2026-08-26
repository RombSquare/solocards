package com.rombsquare.solocards.core.domain.models

// For GenerateQuiz
sealed class GeneratingResult {
    data class Success(val cards: List<GeneratedCard>): GeneratingResult()
    data class Failure(val reason: String, val wrongCard: Card): GeneratingResult()
}