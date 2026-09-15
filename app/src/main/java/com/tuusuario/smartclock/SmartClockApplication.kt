package com.tuusuario.smartclock

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmartClockApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

