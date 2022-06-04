package com.photogallery.app

import android.app.Application
import com.photogallery.app.data.dataModule
import com.photogallery.app.domain.domainModule
import com.photogallery.app.presentation.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(
                listOf(dataModule, domainModule, presentationModule)
            )
        }
    }
}