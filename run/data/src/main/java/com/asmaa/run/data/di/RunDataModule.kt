package com.asmaa.run.data.di

import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module
import com.asmaa.run.data.CreateRunWorker
import com.asmaa.run.data.FetchRunsWorker
import com.asmaa.run.data.DeleteRunWorker

val runDatatModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)
}