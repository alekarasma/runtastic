package com.asmaa.runtastic

import android.app.Application
import com.asmaa.auth.data.di.authDataModule
import com.asmaa.auth.presentation.di.authViewModelModule
import com.asmaa.core.data.di.CoreDataModule
import com.asmaa.runtastic.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class RuntasticApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RuntasticApp)
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                CoreDataModule
            )
        }
    }


}