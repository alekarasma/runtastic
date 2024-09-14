package com.asmaa.run.presentation

import com.asmaa.run.presentation.run_overview.RunUi

data class RunOverviewState(
    val runs: List<RunUi> = emptyList()
)