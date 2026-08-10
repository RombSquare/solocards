package com.rombsquare.solocards.domain.usecases.cards

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.Quiz
import com.rombsquare.solocards.domain.repos.DataRepo

class UpdateCard(
    private val repo: DataRepo
) {
    suspend operator fun invoke(card: Card) {
        return repo.updateCard(card)
    }
}