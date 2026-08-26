package com.rombsquare.solocards.features.cloud_feature.ui.models

import com.google.firebase.auth.FirebaseUser
import kotlin.time.Clock
import kotlin.time.Instant

data class UiState(
    val user: FirebaseUser? = null,
    val dateModified: Instant = Instant.DISTANT_PAST,
    val quizCount: Int = 0
)