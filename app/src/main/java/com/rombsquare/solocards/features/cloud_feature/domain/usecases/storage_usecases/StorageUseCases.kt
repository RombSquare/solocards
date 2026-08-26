package com.rombsquare.solocards.features.cloud_feature.domain.usecases.storage_usecases

import com.rombsquare.solocards.features.cloud_feature.domain.models.MiscData

data class StorageUseCases(
    val exportData: ExportData,
    val importData: ImportData,
    val getMisc: GetMisc
)
