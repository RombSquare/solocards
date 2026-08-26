package com.rombsquare.solocards.features.game_feature.di

import com.rombsquare.solocards.features.game_feature.domain.usecases.game_cards_usecases.GameCardsUseCases
import com.rombsquare.solocards.features.game_feature.domain.usecases.game_cards_usecases.GenerateOptions
import com.rombsquare.solocards.features.game_feature.domain.usecases.game_cards_usecases.GenerateRandomMode
import com.rombsquare.solocards.features.game_feature.ui.GameViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val gameModule = module {
    viewModel { GameViewModel(get(), get(), get(), get(), get()) }

    factory {
        GameCardsUseCases(
            generateOptions = GenerateOptions(),
            generateRandomMode = GenerateRandomMode()
        )
    }
}