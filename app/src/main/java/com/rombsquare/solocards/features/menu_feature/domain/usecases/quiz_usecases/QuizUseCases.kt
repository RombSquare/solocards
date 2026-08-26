package com.rombsquare.solocards.features.menu_feature.domain.usecases.quiz_usecases

import com.rombsquare.solocards.features.editor_feature.domain.usecases.quiz_settings_usecases.UpdateModifiedDate

data class QuizUseCases(
    val insertQuiz: InsertQuiz,
    val updateQuiz: UpdateQuiz,
    val deleteQuiz: DeleteQuiz,
    val generateExamples: GenerateExamples,
    val resetQuizzes: ResetQuizzes,
    val changeQuizFav: ChangeQuizFav,
    val getAllTags: GetAllTags,
    val moveQuizToTrash: MoveQuizToTrash,
    val getQuizzesBySection: GetQuizzesBySection,
    val clearTrash: ClearTrash,
    val renameQuiz: RenameQuiz,
    val restoreQuiz: RestoreQuiz,
    val updateTagsOfQuiz: UpdateTagsOfQuiz,
    val addTagToQuiz: AddTagToQuiz,
    val removeTagFromQuiz: RemoveTagFromQuiz,
    val archiveQuiz: ArchiveQuiz,
    val updateModifiedDate: UpdateModifiedDate
)
