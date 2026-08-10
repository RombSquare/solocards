package com.rombsquare.solocards.domain.usecases.cards

data class CardUseCases(
    val insertCard: InsertCard,
    val getCardsByQuiz: GetCardsByQuiz,
    val updateCard: UpdateCard,
    val deleteCard: DeleteCard,
    val createEmptyCard: CreateEmptyCard,
    val updateCardText: UpdateCardText,
    val generateOptions: GenerateOptions,
    val generateRandomMode: GenerateRandomMode
)
