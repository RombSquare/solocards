package com.rombsquare.solocards.di

import androidx.room.Room
import com.rombsquare.solocards.data.lua.ScriptRepoImpl
import com.rombsquare.solocards.data.room.AppDatabase
import com.rombsquare.solocards.data.room.DataRepoImpl
import com.rombsquare.solocards.domain.CardGenerator
import com.rombsquare.solocards.domain.repos.DataRepo
import com.rombsquare.solocards.domain.repos.ScriptRepo
import com.rombsquare.solocards.domain.usecases.card_validation.BasicCardValidation
import com.rombsquare.solocards.domain.usecases.card_validation.BooleanModeValidation
import com.rombsquare.solocards.domain.usecases.card_validation.CardTextValidation
import com.rombsquare.solocards.domain.usecases.card_validation.CardValidationUseCases
import com.rombsquare.solocards.domain.usecases.card_validation.EditCardValidation
import com.rombsquare.solocards.domain.usecases.card_validation.MixedModeValidation
import com.rombsquare.solocards.domain.usecases.card_validation.OptionModeValidation
import com.rombsquare.solocards.domain.usecases.cards.CardUseCases
import com.rombsquare.solocards.domain.usecases.cards.CreateEmptyCard
import com.rombsquare.solocards.domain.usecases.cards.DeleteCard
import com.rombsquare.solocards.domain.usecases.cards.GenerateOptions
import com.rombsquare.solocards.domain.usecases.cards.GenerateRandomMode
import com.rombsquare.solocards.domain.usecases.cards.GetCardsByQuiz
import com.rombsquare.solocards.domain.usecases.cards.InsertCard
import com.rombsquare.solocards.domain.usecases.cards.UpdateCard
import com.rombsquare.solocards.domain.usecases.cards.UpdateCardText
import com.rombsquare.solocards.domain.usecases.history.DeleteAllByQuiz
import com.rombsquare.solocards.domain.usecases.history.DeleteGameResult
import com.rombsquare.solocards.domain.usecases.history.GetResultsByQuiz
import com.rombsquare.solocards.domain.usecases.history.HistoryUseCases
import com.rombsquare.solocards.domain.usecases.history.InsertGameResult
import com.rombsquare.solocards.domain.usecases.quizzes.AddTagToQuiz
import com.rombsquare.solocards.domain.usecases.quizzes.ArchiveQuiz
import com.rombsquare.solocards.domain.usecases.quizzes.ChangeQuizFav
import com.rombsquare.solocards.domain.usecases.quizzes.ClearTrash
import com.rombsquare.solocards.domain.usecases.quizzes.DeleteQuiz
import com.rombsquare.solocards.domain.usecases.quizzes.GenerateExamples
import com.rombsquare.solocards.domain.usecases.quizzes.GetAllTags
import com.rombsquare.solocards.domain.usecases.quizzes.GetQuiz
import com.rombsquare.solocards.domain.usecases.quizzes.GetQuizzesBySection
import com.rombsquare.solocards.domain.usecases.quizzes.InsertQuiz
import com.rombsquare.solocards.domain.usecases.quizzes.MoveQuizToTrash
import com.rombsquare.solocards.domain.usecases.quizzes.QuizUseCases
import com.rombsquare.solocards.domain.usecases.quizzes.RemoveTagFromQuiz
import com.rombsquare.solocards.domain.usecases.quizzes.RenameQuiz
import com.rombsquare.solocards.domain.usecases.quizzes.ResetQuizzes
import com.rombsquare.solocards.domain.usecases.quizzes.RestoreQuiz
import com.rombsquare.solocards.domain.usecases.quizzes.UpdateModifiedDate
import com.rombsquare.solocards.domain.usecases.quizzes.UpdateQuiz
import com.rombsquare.solocards.domain.usecases.quizzes.UpdateTagsOfQuiz
import com.rombsquare.solocards.domain.usecases.scripting.GenerateCard
import com.rombsquare.solocards.domain.usecases.scripting.GenerateQuiz
import com.rombsquare.solocards.domain.usecases.scripting.ScriptUseCases
import com.rombsquare.solocards.ui.screens.editor.EditorViewModel
import com.rombsquare.solocards.ui.screens.game.GameViewModel
import com.rombsquare.solocards.ui.screens.menu.MenuViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    single { CardGenerator(get()) }

    // Daos

    single { get<AppDatabase>().quizDao() }
    single { get<AppDatabase>().cardDao() }
    single { get<AppDatabase>().gameResultDao() }

    // Repos

    single<DataRepo> { DataRepoImpl(get(), get(), get()) }
    single<ScriptRepo> { ScriptRepoImpl() }

    // ViewModels

    viewModel { MenuViewModel(get()) }
    viewModel { EditorViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { GameViewModel(get(), get(), get(), get(), get()) }

    // Use cases

    factory {
        ScriptUseCases(
            generateCard = GenerateCard(get()),
            generateQuiz = GenerateQuiz(get())
        )
    }

    factory {
        QuizUseCases(
            insertQuiz = InsertQuiz(get()),
            getQuiz = GetQuiz(get()),
            updateQuiz = UpdateQuiz(get()),
            deleteQuiz = DeleteQuiz(get()),
            generateExamples = GenerateExamples(get()),
            resetQuizzes = ResetQuizzes(get()),
            changeQuizFav = ChangeQuizFav(get()),
            getAllTags = GetAllTags(get()),
            moveQuizToTrash = MoveQuizToTrash(get()),
            getQuizzesBySection = GetQuizzesBySection(get()),
            clearTrash = ClearTrash(get()),
            renameQuiz = RenameQuiz(get()),
            restoreQuiz = RestoreQuiz(get()),
            updateTagsOfQuiz = UpdateTagsOfQuiz(get()),
            addTagToQuiz = AddTagToQuiz(get()),
            removeTagFromQuiz = RemoveTagFromQuiz(get()),
            archiveQuiz = ArchiveQuiz(get()),
            updateModifiedDate = UpdateModifiedDate(get())
        )
    }

    factory {
        CardUseCases(
            insertCard = InsertCard(get()),
            getCardsByQuiz = GetCardsByQuiz(get()),
            updateCard = UpdateCard(get()),
            deleteCard = DeleteCard(get()),
            createEmptyCard = CreateEmptyCard(get()),
            updateCardText = UpdateCardText(get()),
            generateOptions = GenerateOptions(),
            generateRandomMode = GenerateRandomMode()
        )
    }

    factory {
        HistoryUseCases(
            insertGameResult = InsertGameResult(get()),
            deleteGameResult = DeleteGameResult(get()),
            getResultsByQuiz = GetResultsByQuiz(get()),
            deleteAllByQuiz = DeleteAllByQuiz(get())
        )
    }

    factory {
        CardValidationUseCases(
            basicCardValidation = BasicCardValidation(),
            booleanModeValidation = BooleanModeValidation(),
            editCardValidation = EditCardValidation(),
            mixedModeValidation = MixedModeValidation(),
            optionModeValidation = OptionModeValidation(),
            cardTextValidation = CardTextValidation()
        )
    }
}