package com.rombsquare.solocards.features.game_feature.ui.models

import com.rombsquare.solocards.core.domain.models.CardSide
import com.rombsquare.solocards.core.domain.models.GameMode
import com.rombsquare.solocards.core.domain.models.GeneratedCard
import com.rombsquare.solocards.core.domain.models.Quiz
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class UiState(
    val quiz: Quiz? = null,
    val cards: List<GeneratedCard> = emptyList(),
    val currentCard: GeneratedCard? = null,
    val cardSide: CardSide = CardSide.Question,
    val cardText: String = "Loading...",
    val solved: Int = 0,
    val cardCount: Int = 0,
    val time: Duration = 0.seconds,
    val isEnd: Boolean = false,
    val isError: Boolean = false,
    val goHome: Boolean = false, // If true, leave the screen
    val dialog: Dialog? = null,
    val mode: GameMode = GameMode.Flip,

    // For boolean mode
    val hypotheticalAnswer: String = "",

    // For option mode
    val options: List<String> = emptyList()
)