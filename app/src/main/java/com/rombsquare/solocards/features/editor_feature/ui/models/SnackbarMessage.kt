package com.rombsquare.solocards.features.editor_feature.ui.models

import com.rombsquare.solocards.core.domain.models.CardValidationError

sealed interface SnackbarMessage {
    data class OnFailedValidation(val validationError: CardValidationError): SnackbarMessage
    data class Code(val question: String, val answer: String): SnackbarMessage
    data class CodeError(val reason: String): SnackbarMessage

    // Other messages
    data object HistoryCleared: SnackbarMessage
    data object CardIsDeleted: SnackbarMessage
    data object CannotDeleteSingleCard: SnackbarMessage
    data object CardCreated: SnackbarMessage
}