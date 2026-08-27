package com.rombsquare.solocards.features.cloud_feature.domain.repos

import com.rombsquare.solocards.features.menu_feature.domain.models.Progress
import com.rombsquare.solocards.features.cloud_feature.domain.models.MiscData

interface CloudStorageRepo {
    fun exportData(progress: Progress)
    fun importData(onResult: (Progress?) -> Unit)
    suspend fun getMisc(onResult: (MiscData?) -> Unit)
}