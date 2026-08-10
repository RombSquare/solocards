package com.rombsquare.solocards.domain.usecases.cards

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.repos.DataRepo

class DeleteCard(
    private val repo: DataRepo
) {
    suspend operator fun invoke(card: Card) {
        repo.deleteCard(card)
    }
}