package com.example.textme

import android.app.Application
import com.example.textme.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TextMeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin { androidLogger()
        androidContext(this@TextMeApplication)
        modules(appModule)}
    }
}