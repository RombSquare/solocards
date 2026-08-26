package com.rombsquare.solocards.features.editor_feature.ui.models

sealed interface UiEffect {
    data class ShowSnackbar(val message: SnackbarMessage): UiEffect
}