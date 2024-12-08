package com.asmaa.run.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.asmaa.run.presentation.run_overview.RunOverviewViewModel
import com.asmaa.run.presentation.active_run.ActiveRunViewModel
import org.koin.core.module.dsl.singleOf
import com.asmaa.run.domain.RunningTracker

val runViewModelModule = module {
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
    singleOf(::RunningTracker)
}