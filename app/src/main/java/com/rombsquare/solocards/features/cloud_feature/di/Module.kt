package com.rombsquare.solocards.features.cloud_feature.di

import com.rombsquare.solocards.features.cloud_feature.data.FirestoreRepo
import com.rombsquare.solocards.features.cloud_feature.data.GoogleAuthRepo
import com.rombsquare.solocards.features.cloud_feature.domain.repos.AuthRepo
import com.rombsquare.solocards.features.cloud_feature.domain.repos.CloudStorageRepo
import com.rombsquare.solocards.features.cloud_feature.domain.usecases.storage_usecases.ExportData
import com.rombsquare.solocards.features.cloud_feature.domain.usecases.storage_usecases.GetMisc
import com.rombsquare.solocards.features.cloud_feature.domain.usecases.storage_usecases.ImportData
import com.rombsquare.solocards.features.cloud_feature.domain.usecases.storage_usecases.StorageUseCases
import com.rombsquare.solocards.features.cloud_feature.ui.CloudViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val cloudModule = module {
    single<AuthRepo> { GoogleAuthRepo(get()) }
    single<CloudStorageRepo> { FirestoreRepo() }

    factory {
        StorageUseCases(
            exportData = ExportData(get(), get()),
            importData = ImportData(get(), get()),
            getMisc = GetMisc(get())
        )
    }

    viewModel { CloudViewModel(get(), get()) }
}