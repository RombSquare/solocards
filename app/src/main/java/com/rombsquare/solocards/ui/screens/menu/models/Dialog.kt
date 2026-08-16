package com.rombsquare.solocards.ui.screens.menu.models

sealed interface Dialog {
    data object CreateQuiz: Dialog
    data object RenameQuiz: Dialog
    data object DeleteWarning: Dialog
    data object ClearTrash: Dialog
    data object RestoreQuiz: Dialog
    data object Tag: Dialog
    data object Settings: Dialog
    data object ImportProgress: Dialog
    data object ResetProgress: Dialog
}