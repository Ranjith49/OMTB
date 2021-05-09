package com.koa2.omdb

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OMDBApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(AppConstants.TAG, "onCreate Application")
    }
}