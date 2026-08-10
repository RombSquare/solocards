package com.rombsquare.solocards.ui.screens.editor.models

sealed interface Dialog {
    data object Code: Dialog
    data object DeleteWarning: Dialog
    data object Play: Dialog
    data object History: Dialog
    data object Props: Dialog
    data object Settings: Dialog
}