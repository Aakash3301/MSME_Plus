package com.msme.plus

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MsmeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@MsmeApp)
            modules(com.msme.plus.shared.core.di.sharedModule)
        }
    }
}
