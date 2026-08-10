package com.rombsquare.solocards.domain.usecases.history

data class HistoryUseCases(
    val insertGameResult: InsertGameResult,
    val deleteGameResult: DeleteGameResult,
    val getResultsByQuiz: GetResultsByQuiz,
    val deleteAllByQuiz: DeleteAllByQuiz,
)