package com.asmaa.core.data.di

import com.asmaa.core.data.auth.EncryptedSessionStorage
import com.asmaa.core.data.networking.HttpClientFactory
import com.asmaa.core.domain.SessionStorage
import com.asmaa.core.data.run.OfflineFirstRunRepository
import com.asmaa.core.domain.run.RunRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val CoreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()

    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}