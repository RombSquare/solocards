package com.rombsquare.solocards.domain.usecases.quizzes

data class QuizUseCases(
    val insertQuiz: InsertQuiz,
    val getQuiz: GetQuiz,
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
