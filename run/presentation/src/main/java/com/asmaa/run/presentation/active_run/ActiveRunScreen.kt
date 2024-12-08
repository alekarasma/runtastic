package com.asmaa.run.presentation.active_run

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asmaa.core.presentation.designsystem.RuntasticTheme
import com.asmaa.core.presentation.designsystem.StartIcon
import com.asmaa.core.presentation.designsystem.StopIcon
import com.asmaa.core.presentation.designsystem.components.RuntasticActionButton
import com.asmaa.core.presentation.designsystem.components.RuntasticDialog
import com.asmaa.core.presentation.designsystem.components.RuntasticFloatingActionButton
import com.asmaa.core.presentation.designsystem.components.RuntasticOutlinedActionButton
import com.asmaa.core.presentation.designsystem.components.RuntasticScaffold
import com.asmaa.core.presentation.designsystem.components.RuntasticToolbar
import com.asmaa.core.presentation.ui.ObserveAsEvents
import com.asmaa.run.presentation.R
import com.asmaa.run.presentation.active_run.components.RunDataCard
import com.asmaa.run.presentation.active_run.maps.TrackerMap
import com.asmaa.run.presentation.util.hasLocationPermission
import com.asmaa.run.presentation.util.hasNotificationPermission
import com.asmaa.run.presentation.util.shouldShowLocationPermission
import com.asmaa.run.presentation.util.shouldShowNotificationPermission
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream


@Composable
fun ActiveRunScreenRoot(
    onBack: () -> Unit,
    onFinish: () -> Unit,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    viewModel: ActiveRunViewModel = koinViewModel()
) {

    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is ActiveRunEvent.Error -> Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
            ActiveRunEvent.RunSaved -> onFinish()
        }
    }
    ActiveRunScreen(viewModel.state, onServiceToggle = onServiceToggle, onAction = { action ->
        when (action) {
            is ActiveRunAction.OnBackClick -> {
                if (!viewModel.state.hasStartedRunning) {
                    onBack()
                }
            }

            else -> {
                Unit
            }
        }
        viewModel.onAction(action)
    })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveRunScreen(
    state: ActiveRunState,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            val hasCourseLocation = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            val hasFineLocation = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val hasNotification = if (Build.VERSION.SDK_INT >= 33) {
                perms[Manifest.permission.POST_NOTIFICATIONS] == true
            } else {
                true
            }
            val activity = context as ComponentActivity
            val showLocationRationale = activity.shouldShowLocationPermission()
            val showNotificationRationale = activity.shouldShowNotificationPermission()
            onAction(
                ActiveRunAction.SubmitLocationPermissionInfo(
                    hasCourseLocation && hasFineLocation,
                    showLocationRationale
                )
            )
            onAction(
                ActiveRunAction.SubmitNotificationPermissionInfo(
                    hasNotification,
                    showNotificationRationale
                )
            )
        }
    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermission()
        val showNotificationRationale = activity.shouldShowNotificationPermission()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                context.hasLocationPermission(),
                showLocationRationale
            )
        )
        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                context.hasNotificationPermission(),
                showNotificationRationale
            )
        )
        if (!showLocationRationale && !showNotificationRationale) {
            permissionLauncher.requestRuntasticPermissions(context)
        }

    }
    LaunchedEffect(key1 = state.isRunFinished) {
        if (state.isRunFinished) {
            onServiceToggle(false)
        }
    }

    LaunchedEffect(key1 = state.shouldTrack) {
        if (context.hasLocationPermission() && state.shouldTrack && !ActiveRunService.isServiceActive) {
            onServiceToggle(true)
        }

    }

    RuntasticScaffold(
        withGradient = false, topAppBar = {
            RuntasticToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClick = { onAction(ActiveRunAction.OnBackClick) }
            )
        },
        floatingActionButton = {
            RuntasticFloatingActionButton(
                icon = if (state.shouldTrack) {
                    StopIcon
                } else {
                    StartIcon
                },
                onClick = { onAction(ActiveRunAction.OnToggleRunClick) },
                iconSize = 20.dp,
                contentDescription = if (state.shouldTrack) {
                    stringResource(id = R.string.pause_run)
                } else {
                    stringResource(id = R.string.start_run)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TrackerMap(
                isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runData.location,
                onSnapshot = { bmp ->
                    val stream = ByteArrayOutputStream()
                    stream.use {
                        bmp.compress(
                            Bitmap.CompressFormat.JPEG, 80, it
                        )
                    }
                    onAction(ActiveRunAction.OnRunProcesses(stream.toByteArray()))
                }

            )
            RunDataCard(
                elapsedTime = state.elapsedTime, runData = state.runData, modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            )
        }
    }
    if (!state.shouldTrack && state.hasStartedRunning) {
        RuntasticDialog(
            title = stringResource(id = R.string.running_is_paused),
            onDismiss = { onAction(ActiveRunAction.OnResumeRunClick) },
            description = stringResource(id = R.string.resume_or_finish_run),
            primaryButton = {
                RuntasticActionButton(
                    text = stringResource(id = R.string.resume),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.OnResumeRunClick)
                    }, modifier = Modifier.weight(1f)
                )


            },
            secondaryButton = {
                RuntasticOutlinedActionButton(
                    text = stringResource(id = R.string.finish),
                    isLoading = state.isSavingRun,
                    onClick = {
                        onAction(
                            ActiveRunAction.OnFinishRunClick
                        )
                    }, modifier = Modifier.weight(1f)
                )
            }
        )
    }

    if (state.showLocationRationale || state.showNotificationRationale) {
        RuntasticDialog(
            title = stringResource(id = R.string.permission_required),
            onDismiss = { /*TODO*/ },
            description = when {
                state.showLocationRationale && state.showNotificationRationale -> {
                    stringResource(id = R.string.location_notification_rationale)
                }

                state.showLocationRationale -> {
                    stringResource(id = R.string.location_rationale)
                }

                else -> {
                    stringResource(id = R.string.notification_rationale)
                }
            },
            primaryButton = {
                RuntasticOutlinedActionButton(
                    text = stringResource(id = R.string.ok),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.DismissRationalDialog)
                        permissionLauncher.requestRuntasticPermissions(context)
                    })
            })
    }
}

private fun ActivityResultLauncher<Array<String>>.requestRuntasticPermissions(context: Context) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val notificationPermission = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else arrayOf()

    when {
        !hasLocationPermission && !hasNotificationPermission -> {
            launch(locationPermissions + notificationPermission)
        }

        !hasLocationPermission -> locationPermissions
        !hasNotificationPermission -> notificationPermission
    }

}

@Preview
@Composable
private fun ActiveRunScreenPreview() {
    RuntasticTheme {
        ActiveRunScreen(state = ActiveRunState(), {}) {

        }
    }
}