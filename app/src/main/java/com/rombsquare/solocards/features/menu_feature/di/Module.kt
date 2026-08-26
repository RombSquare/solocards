package com.rombsquare.solocards.features.menu_feature.di

import com.rombsquare.solocards.features.editor_feature.domain.usecases.quiz_settings_usecases.UpdateModifiedDate
import com.rombsquare.solocards.features.menu_feature.data.json.SerializerRepoImpl
import com.rombsquare.solocards.features.menu_feature.domain.repos.SerializerRepo
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.AddTagToQuiz
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.ArchiveQuiz
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.ChangeQuizFav
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.ClearTrash
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.DeleteQuiz
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.GenerateExamples
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.GetAllTags
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.GetQuizzesBySection
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.InsertQuiz
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.MoveQuizToTrash
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.QuizUseCases
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.RemoveTagFromQuiz
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.RenameQuiz
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.ResetQuizzes
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.RestoreQuiz
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.UpdateQuiz
import com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases.UpdateTagsOfQuiz
import com.rombsquare.solocards.features.menu_feature.domain.usecases.serializer_usecases.DeserializeProgress
import com.rombsquare.solocards.features.menu_feature.domain.usecases.serializer_usecases.DeserializeQuiz
import com.rombsquare.solocards.features.menu_feature.domain.usecases.serializer_usecases.SerializeProgress
import com.rombsquare.solocards.features.menu_feature.domain.usecases.serializer_usecases.SerializeQuiz
import com.rombsquare.solocards.features.menu_feature.domain.usecases.serializer_usecases.SerializerUseCases
import com.rombsquare.solocards.features.menu_feature.ui.MenuViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val menuModule = module {
    single<SerializerRepo> { SerializerRepoImpl() }
    viewModel { MenuViewModel(get(), get(), get(), get()) }

    factory {
        QuizUseCases(
            insertQuiz = InsertQuiz(get()),
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
        SerializerUseCases(
            serializeQuiz = SerializeQuiz(get(), get()),
            deserializeQuiz = DeserializeQuiz(get(), get()),
            serializeProgress = SerializeProgress(get(), get()),
            deserializeProgress = DeserializeProgress(get(), get())
        )
    }
}