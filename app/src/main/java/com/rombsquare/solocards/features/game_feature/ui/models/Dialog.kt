package com.rombsquare.solocards.features.game_feature.ui.models

import com.rombsquare.solocards.core.domain.models.Card

sealed interface Dialog {
    data class End(
        val score: Double,
        val correct: Int,
        val maybeCorrect: Int,
        val incorrect: Int,
    ): Dialog

    data class Error(
        val reason: String,
        val wrongCard: Card,
    ): Dialog

    data object Satisfaction: Dialog

    data object ExitDialog: Dialog
}