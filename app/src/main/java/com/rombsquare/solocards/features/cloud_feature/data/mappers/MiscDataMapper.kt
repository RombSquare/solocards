package com.rombsquare.solocards.features.cloud_feature.data.mappers

import com.rombsquare.solocards.features.cloud_feature.data.models.MiscDataObject
import com.rombsquare.solocards.features.cloud_feature.domain.models.MiscData
import kotlin.time.toKotlinInstant

//fun MiscData.toFirestoreObject() = MiscDataObject(
//    Timestamp(this.modifiedAt.toJavaInstant()),
//    this.quizCount
//)

fun MiscDataObject.toDomain() = MiscData(
    this.modifiedAt.toInstant().toKotlinInstant(),
    this.quizCount
)