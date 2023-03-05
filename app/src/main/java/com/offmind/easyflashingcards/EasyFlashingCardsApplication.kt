package com.offmind.easyflashingcards

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class EasyFlashingCardsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@EasyFlashingCardsApplication)
            modules(modules = appModules)
        }
    }
}