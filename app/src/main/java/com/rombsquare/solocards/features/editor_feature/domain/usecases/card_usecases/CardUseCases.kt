package com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases

data class CardUseCases(
    val insertCard: InsertCard,
    val getCardsByQuiz: GetCardsByQuiz,
    val updateCard: UpdateCard,
    val deleteCard: DeleteCard,
    val createEmptyCard: CreateEmptyCard,
    val updateCardText: UpdateCardText,
)
