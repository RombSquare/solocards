package com.rombsquare.solocards.ui.screens.editor.models

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.CardSide
import com.rombsquare.solocards.domain.models.GameMode
import com.rombsquare.solocards.domain.models.GameResult
import com.rombsquare.solocards.domain.models.Quiz

data class UiState(
    val quizId: Long? = null,
    val quiz: Quiz = Quiz(),
    val currentCard: Card? = null,
    val cardSide: CardSide = CardSide.Question,
    val cardText: String = "Loading...",
    val index: Int = 0,
    val cardCount: Int = 0,
    val dialog: Dialog? = null,

    // Moving to game screen
    val moveToGame: Boolean = false,
    val chosenMode: GameMode = GameMode.Flip,

    // Toast message
    val message: String = "",

    // History feature
    val gameResults: List<GameResult> = emptyList(),
    val selectedGameResult: GameResult? = null,
)