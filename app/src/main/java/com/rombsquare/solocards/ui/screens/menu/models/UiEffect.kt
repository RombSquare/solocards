package com.rombsquare.solocards.ui.screens.menu.models

sealed interface UiEffect {
    data class CreateDocument(val name: String): UiEffect
    data object OpenDocument: UiEffect
    data class ShareJson(val jsonString: String, val name: String): UiEffect
}