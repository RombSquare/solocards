package com.rombsquare.solocards.features.cloud_feature.domain.usecases.storage_usecases

import com.rombsquare.solocards.features.cloud_feature.domain.models.MiscData
import com.rombsquare.solocards.features.cloud_feature.domain.repos.CloudStorageRepo

class GetMisc(
    val repo: CloudStorageRepo
) {
    suspend operator fun invoke(onResult: (MiscData?) -> Unit) {
        repo.getMisc { miscData ->
            if (miscData == null) {
                onResult(null)
                return@getMisc
            }

            onResult(miscData)
        }
    }
}