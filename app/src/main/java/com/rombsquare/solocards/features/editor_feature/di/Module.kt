package com.rombsquare.solocards.features.editor_feature.di

import com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases.CardUseCases
import com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases.CreateEmptyCard
import com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases.DeleteCard
import com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases.GetCardsByQuiz
import com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases.InsertCard
import com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases.UpdateCard
import com.rombsquare.solocards.features.editor_feature.domain.usecases.card_usecases.UpdateCardText
import com.rombsquare.solocards.features.editor_feature.domain.usecases.quiz_settings_usecases.QuizSettingsUseCases
import com.rombsquare.solocards.features.editor_feature.domain.usecases.quiz_settings_usecases.UpdateModifiedDate
import com.rombsquare.solocards.features.editor_feature.domain.usecases.quiz_settings_usecases.UpdateSettings
import com.rombsquare.solocards.features.editor_feature.ui.EditorViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val editorModule = module {
    viewModel { EditorViewModel(get(), get(), get(), get(), get(), get(), get()) }

    factory {
        CardUseCases(
            insertCard = InsertCard(get()),
            getCardsByQuiz = GetCardsByQuiz(get()),
            updateCard = UpdateCard(get()),
            deleteCard = DeleteCard(get()),
            createEmptyCard = CreateEmptyCard(get()),
            updateCardText = UpdateCardText(get()),
        )
    }

    factory {
        QuizSettingsUseCases(
            updateModifiedDate = UpdateModifiedDate(get()),
            updateSettings = UpdateSettings(get())
        )
    }
}