package com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases

import com.rombsquare.solocards.core.domain.models.Card
import com.rombsquare.solocards.core.domain.repos.DataRepo

class DeleteCard(
    private val repo: DataRepo
) {
    suspend operator fun invoke(card: Card) {
        repo.deleteCard(card)
    }
}