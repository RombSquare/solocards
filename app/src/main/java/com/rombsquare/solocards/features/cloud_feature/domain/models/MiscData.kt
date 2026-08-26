package com.rombsquare.solocards.features.cloud_feature.domain.models

import kotlin.time.Instant

// It used to show when the cloud data was modified and how many quizzes there
data class MiscData(
    val modifiedAt: Instant,
    val quizCount: Int
)