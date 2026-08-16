package com.rombsquare.solocards.domain.models

enum class QuizSortMethod {
    ByName,
    ByDateCreated,
    ByDateModified,
}

enum class SortDirection {
    Ascending,
    Descending
}

data class QuizSortOptions(
    val method: QuizSortMethod,
    val direction: SortDirection,
    val moveFavoritesToTop: Boolean
)