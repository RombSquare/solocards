package com.rombsquare.solocards.core.domain.usecases.history

data class HistoryUseCases(
    val insertGameResult: InsertGameResult,
    val deleteGameResult: DeleteGameResult,
    val getResultsByQuiz: GetResultsByQuiz,
    val deleteAllByQuiz: DeleteAllByQuiz,
)