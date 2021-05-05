package com.example.enoctest.application

import android.app.Application
import com.example.enoctest.BuildConfig
import timber.log.Timber

class EnocTest : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        var instance: Application? = null
    }
}