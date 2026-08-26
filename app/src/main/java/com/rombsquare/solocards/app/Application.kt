package com.rombsquare.solocards.app

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.rombsquare.solocards.core.di.coreModule
import com.rombsquare.solocards.features.cloud_feature.di.cloudModule
import com.rombsquare.solocards.features.editor_feature.di.editorModule
import com.rombsquare.solocards.features.game_feature.di.gameModule
import com.rombsquare.solocards.features.menu_feature.di.menuModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class SolocardsApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SolocardsApp)
            androidLogger()
            modules(
                coreModule,
                menuModule,
                editorModule,
                gameModule,
                cloudModule
            )
        }

        FirebaseApp.initializeApp(this)
        Log.d("FirebaseTest", "FirebaseApp is initialized")
    }
}