package com.asmaa.run.presentation.run_overview

sealed interface RunOverviewAction {
    data object OnStartClick : RunOverviewAction
    data object OnLogoutClick : RunOverviewAction
    data object OnAnalyticsClick : RunOverviewAction
    data class OnDeleteRun(val runUi: RunUi) : RunOverviewAction
}