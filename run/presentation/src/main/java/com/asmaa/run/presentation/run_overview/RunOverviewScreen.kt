@file:OptIn(ExperimentalMaterial3Api::class)

package com.asmaa.run.presentation.run_overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asmaa.core.presentation.designsystem.AnalyticsIcon
import com.asmaa.core.presentation.designsystem.LogoIcon
import com.asmaa.core.presentation.designsystem.LogoutIcon
import com.asmaa.core.presentation.designsystem.RunIcon
import com.asmaa.core.presentation.designsystem.RuntasticTheme
import com.asmaa.core.presentation.designsystem.components.RuntasticFloatingActionButton
import com.asmaa.core.presentation.designsystem.components.RuntasticScaffold
import com.asmaa.core.presentation.designsystem.components.RuntasticToolbar
import com.asmaa.core.presentation.designsystem.utils.DropDownItem
import com.asmaa.run.presentation.R
import com.asmaa.run.presentation.RunOverviewState
import com.asmaa.run.presentation.run_overview.components.RunListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverViewScreenRoot(
    onStartRunClick: () -> Unit,
    viewModel: RunOverviewViewModel = koinViewModel()

) {
    RunOverviewScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                RunOverviewAction.OnLogoutClick -> Unit
                RunOverviewAction.OnStartClick -> onStartRunClick()
                else -> {
                    Unit
                }
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun RunOverviewScreen(
    state: RunOverviewState,
    onAction: (RunOverviewAction) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = topAppBarState)
    RuntasticScaffold(
        topAppBar =
        {
            RuntasticToolbar(
                showBackButton = false,
                title = stringResource(id = R.string.runtastic),
                scrollBarBehavior = scrollBehavior,
                menuItems = listOf(
                    DropDownItem(logo = AnalyticsIcon, title = "Analytics"),
                    DropDownItem(logo = LogoutIcon, title = "LogOut")
                ),
                onMenuItemClick = { index ->
                    when (index) {
                        0 -> onAction(RunOverviewAction.OnAnalyticsClick)
                        1 -> onAction(RunOverviewAction.OnLogoutClick)
                    }

                },
                startContent = {
                    Icon(
                        imageVector = LogoIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            RuntasticFloatingActionButton(
                icon = RunIcon,
                onClick = {
                    onAction(RunOverviewAction.OnStartClick)
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(horizontal = 16.dp),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = state.runs, key = { it.id }) {
                RunListItem(
                    runUi = it, onDeleteClick = {
                        onAction(
                            RunOverviewAction.OnDeleteRun(it)
                        )
                    },
                    modifier = Modifier.animateItemPlacement()
                )

            }

        }
    }
}

@Preview
@Composable
private fun ScreenPreview() {
    RuntasticTheme {
        RunOverviewScreen(
            state = RunOverviewState(),
            onAction = {}
        )
    }
}