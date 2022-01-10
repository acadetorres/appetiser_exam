package com.acdetorres.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    //Required by dagger hilt
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}