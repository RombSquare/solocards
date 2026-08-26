package com.rombsquare.solocards.features.cloud_feature.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class MiscDataObject(
    val modifiedAt: Timestamp = Timestamp.now(),
    val quizCount: Int = 0
)
