package com.rombsquare.solocards.ui.screens.menu.models

sealed class Dialog {
    object CreateQuiz: Dialog()
    object RenameQuiz: Dialog()
    object DeleteWarning: Dialog()
    object ClearTrash: Dialog()
    object RestoreQuiz: Dialog()
    object TagDialog: Dialog()
}