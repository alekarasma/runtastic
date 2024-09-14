package com.asmaa.auth.data.di

import com.asmaa.auth.data.EmailPatternValidator
import com.asmaa.auth.domain.AuthRepository
import com.asmaa.auth.data.AuthRepositoryImpl
import com.asmaa.auth.domain.PatternValidator
import com.asmaa.auth.domain.UseDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UseDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}