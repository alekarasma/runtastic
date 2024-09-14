package com.asmaa.runtastic

import android.app.Application
import com.asmaa.auth.data.di.authDataModule
import com.asmaa.auth.presentation.di.authViewModelModule
import com.asmaa.core.data.di.CoreDataModule
import com.asmaa.core.database.di.databaseModule
import com.asmaa.run.data.di.runDatatModule
import com.asmaa.run.location.di.locationModule
import com.asmaa.run.network.networkModule
import com.asmaa.run.presentation.di.runViewModelModule
import com.asmaa.runtastic.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class RuntasticApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RuntasticApp)
            workManagerFactory()
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                CoreDataModule,
                runViewModelModule,
                locationModule,
                databaseModule,
                networkModule,
                runDatatModule
            )
        }
    }


}