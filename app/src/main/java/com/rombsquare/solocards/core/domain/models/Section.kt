package com.rombsquare.solocards.core.domain.models

sealed interface Section {
    data object Everything: Section
    data object Favorite: Section
    data object Trash: Section
    data object Archive: Section
    data class Tag(val tag: String): Section
}