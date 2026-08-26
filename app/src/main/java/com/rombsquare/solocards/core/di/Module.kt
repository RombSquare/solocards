package com.rombsquare.solocards.core.di

import androidx.room.Room
import com.rombsquare.solocards.core.data.lua.ScriptRepoImpl
import com.rombsquare.solocards.core.data.room.AppDatabase
import com.rombsquare.solocards.core.data.room.DataRepoImpl
import com.rombsquare.solocards.core.domain.repos.DataRepo
import com.rombsquare.solocards.core.domain.repos.ScriptRepo
import com.rombsquare.solocards.core.domain.usecases.history.DeleteAllByQuiz
import com.rombsquare.solocards.core.domain.usecases.history.DeleteGameResult
import com.rombsquare.solocards.core.domain.usecases.history.GetResultsByQuiz
import com.rombsquare.solocards.core.domain.usecases.history.HistoryUseCases
import com.rombsquare.solocards.core.domain.usecases.history.InsertGameResult
import com.rombsquare.solocards.core.domain.usecases.scripting.GenerateCard
import com.rombsquare.solocards.core.domain.usecases.scripting.GenerateQuiz
import com.rombsquare.solocards.core.domain.usecases.scripting.ScriptUseCases
import com.rombsquare.solocards.core.domain.usecases.validation.card_validation.BasicCardValidation
import com.rombsquare.solocards.core.domain.usecases.validation.card_validation.BooleanModeValidation
import com.rombsquare.solocards.core.domain.usecases.validation.card_validation.CardTextValidation
import com.rombsquare.solocards.core.domain.usecases.validation.card_validation.CardValidationUseCases
import com.rombsquare.solocards.core.domain.usecases.validation.card_validation.EditCardValidation
import com.rombsquare.solocards.core.domain.usecases.validation.card_validation.MixedModeValidation
import com.rombsquare.solocards.core.domain.usecases.validation.card_validation.OptionModeValidation
import com.rombsquare.solocards.core.domain.usecases.validation.quiz_validation.QuizValidation
import com.rombsquare.solocards.core.domain.usecases.validation.quiz_validation.QuizValidationUseCases
import com.rombsquare.solocards.core.domain.usecases.validation.tag_validation.TagValidation
import com.rombsquare.solocards.core.domain.usecases.validation.tag_validation.TagValidationUseCases
import com.rombsquare.solocards.core.domain.utils.CardGenerator
import com.rombsquare.solocards.core.domain.utils.GetQuizById
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
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
    single { GetQuizById(get()) }

    // Daos

    single { get<AppDatabase>().quizDao() }
    single { get<AppDatabase>().cardDao() }
    single { get<AppDatabase>().gameResultDao() }

    // Repos

    single<DataRepo> { DataRepoImpl(get(), get(), get()) }
    single<ScriptRepo> { ScriptRepoImpl() }

    // Use cases
    factory {
        ScriptUseCases(
            generateCard = GenerateCard(get()),
            generateQuiz = GenerateQuiz(get(), get())
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

    factory {
        TagValidationUseCases(
            tagValidation = TagValidation()
        )
    }

    factory {
        QuizValidationUseCases(
            quizValidation = QuizValidation()
        )
    }
}