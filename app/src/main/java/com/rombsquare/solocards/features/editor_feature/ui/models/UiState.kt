package com.rombsquare.solocards.features.editor_feature.ui.models

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.models.CardSide
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.domain.models.Quiz
import com.rombsquare.solocards.core.domain.models.Session

data class UiState(
    val quizId: Long? = null,
    val quiz: Quiz = Quiz(),
    val cards: List<Card> = emptyList(),
    val currentCard: Card? = null,
    val cardSide: CardSide = CardSide.Question,
    val cardText: String = "Loading...",
    val index: Int = 0,
    val cardCount: Int = 0,
    val dialog: Dialog? = null,

    // Moving to game screen
    val moveToGame: Boolean = false,
    val chosenMode: GameMode = GameMode.Flip,

    // History feature
    val sessions: List<Session> = emptyList(),
    val selectedSession: Session? = null,
)