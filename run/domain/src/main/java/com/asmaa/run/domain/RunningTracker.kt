package com.asmaa.run.domain

import com.asmaa.core.domain.Timer
import com.asmaa.core.domain.location.LocationTimeStamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {
    private val _runData = MutableStateFlow(RunData())
    val runData = _runData.asStateFlow()

    private val isTracking = MutableStateFlow(false)
    private val isObservingLocation = MutableStateFlow(false)

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime = _elapsedTime.asStateFlow()

    val currentLocation = isObservingLocation
        .flatMapLatest { isObservingLocation ->
            if (isObservingLocation) {
                locationObserver.observeLocation(1000)
            } else {
                flowOf()
            }
        } //TODO - what is stateIn, when to use it
        .stateIn(applicationScope, SharingStarted.Lazily, null)

    init {
        isTracking.onEach { isTracking ->
            if (!isTracking) {
                val newList = buildList {
                    addAll(runData.value.location)
                    add(emptyList<LocationTimeStamp>())
                }.toList()
                _runData.update {
                    it.copy(location = newList)
                }
            }

        }
            .flatMapLatest { isTracking ->
                if (isTracking) {
                    Timer.timeAndEmit()
                } else flowOf()
            }
            .onEach {
                _elapsedTime.value += it
            }
            .launchIn(applicationScope)
        //TODO - I didnt understand the part will it call time and Emit everytime. I assume yes, then how timerflow is working in a while loop. There will be many flows started isn't it??

        currentLocation
            .filterNotNull() //TODO - whats the difference between combine and combineTransform
            .combineTransform(isTracking) { location, isTracking ->
                if (isTracking) {
                    emit(location)
                }
            }
            .zip(_elapsedTime) { location, elapsedTime ->
                LocationTimeStamp(location, elapsedTime)
            }
            .onEach { location ->
                val currentLocations = runData.value.location

                val lastLocationList = if (currentLocations.isNotEmpty()) {
                    currentLocations.last() + location
                } else {
                    listOf(location)
                }

                val newLocationsList = currentLocations.replaceLast(lastLocationList)
                val distanceMeters = LocationDataCalculator.getTotalDistanceMeters(newLocationsList)
                val distanceKm = distanceMeters / 1000.0
                val currentDuration = location.durationTimestamp
                val avgSecondsPerKm = if (distanceKm == 0.0) {
                    0
                } else {
                    (currentDuration.inWholeSeconds / distanceKm).roundToInt()
                }
                _runData.update {
                    RunData(distanceMeters, avgSecondsPerKm.seconds, newLocationsList)
                }
            }.launchIn(applicationScope)


    }

    fun setIsTracking(isTracking: Boolean) {
        this.isTracking.value = isTracking
    }


    fun startObservation() {
        isObservingLocation.value = true
    }

    fun stopObservation() {
        isObservingLocation.value = false
    }

    private fun <T> List<List<T>>.replaceLast(replacement: List<T>): List<List<T>> {
        if (this.isEmpty()) {
            return listOf(replacement)
        }
        return this.dropLast(1) + listOf(replacement)
    }

    fun finishRun() {
        stopObservation()
        setIsTracking(false)
        _elapsedTime.value = Duration.ZERO
        _runData.value = RunData()
    }
}

