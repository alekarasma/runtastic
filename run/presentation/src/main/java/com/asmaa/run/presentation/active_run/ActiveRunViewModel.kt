package com.asmaa.run.presentation.active_run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asmaa.core.domain.run.Run
import com.asmaa.core.domain.run.RunRepository
import com.asmaa.core.domain.location.Location
import com.asmaa.core.domain.util.Result
import com.asmaa.core.presentation.ui.asUiText
import com.asmaa.run.domain.LocationDataCalculator
import com.asmaa.run.domain.RunningTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime

class ActiveRunViewModel(private val runningTracker: RunningTracker, private val runRepository: RunRepository) : ViewModel() {

    var state by mutableStateOf(ActiveRunState())
        private set

    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    //TODO -7.8 whats the difference between stateflow and normal flow,
    // Philip mentioned it will convert it into normal flow and not stateflow, when to use which?
    private val shouldTrack = snapshotFlow { state.shouldTrack }
        .stateIn(viewModelScope, SharingStarted.Lazily, state.shouldTrack)
    private val hasLocationPermission = MutableStateFlow(false)

    private val isTracking =
        combine(shouldTrack, hasLocationPermission) { shouldTrack, hasPermission ->
            shouldTrack && hasPermission
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)
    //TODO - what is stateIn use for??
    //TODO - what is the difference between launchIn and StateIn, when to use what?

    //TODO -Understand what init really helps with
    //TODO -Understand launch in
    init {
        hasLocationPermission.onEach { hasPermission ->
            if (hasPermission)
                runningTracker.startObservation()
            else
                runningTracker.startObservation()
        }.launchIn(viewModelScope)

        isTracking
            .onEach { isTracking ->
                runningTracker.setIsTracking(isTracking)
            }.launchIn(viewModelScope)

        runningTracker
            .currentLocation
            .onEach {
                state = state.copy(currentLocation = it?.location)
            }.launchIn(viewModelScope)

        runningTracker
            .runData
            .onEach {
                state = state.copy(runData = it)
            }.launchIn(viewModelScope)

        runningTracker
            .elapsedTime
            .onEach {
                state = state.copy(elapsedTime = it)
            }.launchIn(viewModelScope)
    }

    fun onAction(action: ActiveRunAction) {
        when (action) {
            ActiveRunAction.OnBackClick -> state = state.copy(shouldTrack = false)
            ActiveRunAction.OnFinishRunClick -> state = state.copy(isRunFinished = true, isSavingRun = true)
            ActiveRunAction.OnResumeRunClick -> state = state.copy(shouldTrack = true)
            ActiveRunAction.OnToggleRunClick -> state =
                state.copy(hasStartedRunning = true, shouldTrack = !state.shouldTrack)

            is ActiveRunAction.SubmitLocationPermissionInfo -> {
                hasLocationPermission.value = action.acceptedLocationPermission
                state = state.copy(showLocationRationale = action.showLocationRational)
            }

            is ActiveRunAction.SubmitNotificationPermissionInfo -> {
                state = state.copy(showNotificationRationale = action.showNotificationRational)
            }

            ActiveRunAction.DismissRationalDialog -> {
                state = state.copy(showNotificationRationale = false, showLocationRationale = false)
            }

            is ActiveRunAction.OnRunProcesses -> {
                finishRun(action.mapPictureBytes)
            }
        }
    }

    private fun finishRun(mapPictureBytes: ByteArray) {
        val location = state.runData.location
        if (location.isEmpty() || location.first().size <= 1) {
            state = state.copy(isSavingRun = false)
            return
        }
        viewModelScope.launch {
            val run = Run(
                id = null,
                duration = state.elapsedTime,
                dateTimeUtc = ZonedDateTime.now()
                    .withZoneSameInstant(ZoneId.of("UTC")),
                distanceMeters = state.runData.distanceMeters,
                location = state.currentLocation ?: Location(0.0, 0.0),
                maxSpeed = LocationDataCalculator.getMaxSpeedKmh(location),
                totalElevationMeters = LocationDataCalculator.getTotalElevationMeters(location),
                mapPictureUrl = null
            )
            runningTracker.finishRun()
            when (val result = runRepository.upsertRun(run, mapPictureBytes)) {
                is Result.Error -> {
                    eventChannel.send(ActiveRunEvent.Error(result.error.asUiText()))
                }

                is Result.Success -> {
                    eventChannel.send(ActiveRunEvent.RunSaved)
                }
            }
            state = state.copy(isSavingRun = false)
        }
    }
}