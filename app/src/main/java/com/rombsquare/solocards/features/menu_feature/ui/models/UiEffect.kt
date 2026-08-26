package com.rombsquare.solocards.features.menu_feature.ui.models

import com.rombsquare.solocards.core.domain.models.ValidationError

sealed interface UiEffect {
    data class CreateDocument(val name: String): UiEffect
    data object OpenDocument: UiEffect
    data class ShareJson(val jsonString: String, val name: String): UiEffect
    data class ShowSnackbar(val message: SnackbarMessage, val onUndo: (suspend () -> Unit)? = null): UiEffect
    data class ShowValidationErrorToast(val validation: ValidationError): UiEffect
    data class GoToEditor(val quizId: Long): UiEffect
}