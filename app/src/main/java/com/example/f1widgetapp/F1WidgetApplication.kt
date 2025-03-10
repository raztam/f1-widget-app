package com.example.f1widgetapp

import android.app.Application
import com.example.f1widgetapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class F1WidgetApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin
        startKoin {
            androidLogger()
            androidContext(this@F1WidgetApplication)
            modules(appModule)
        }
    }
}