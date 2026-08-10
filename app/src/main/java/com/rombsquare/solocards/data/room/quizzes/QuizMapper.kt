package com.rombsquare.solocards.data.room.quizzes

import com.rombsquare.solocards.domain.models.Quiz

fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}

fun QuizEntity.toDomain() = Quiz(
    this.quizId,
    this.title,
    this.tags
        .split(',')
        .map { it.trim().capitalize() }
        .filter { it.isNotEmpty() },
    this.isFav,
    this.isTrashed,
    this.isArchived,
    this.showAnswer,
    this.shuffleCards,
    this.swapCardSides,
    this.rateQuiz,
    this.showTreatAsCorrect,
    this.cardCount,
    this.createdAt,
    this.modifiedAt,
)

fun Quiz.toEntity() = QuizEntity(
    this.id,
    this.title,
    this.tags.joinToString(",") { it.capitalize() },
    this.isFav,
    this.isTrashed,
    this.isArchived,
    this.showAnswer,
    this.shuffleCards,
    this.swapCardSides,
    this.rateQuiz,
    this.showTreatAsCorrect,
    this.cardCount,
    this.createdAt,
    this.modifiedAt
)