package com.asmaa.auth.presentation.di


import com.asmaa.auth.presentation.register.RegisterViewModel
import com.asmaa.auth.presentation.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}