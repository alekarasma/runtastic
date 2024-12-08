package com.asmaa.core.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

//TODO - what is hot flow and cold flow
object Timer {
    fun timeAndEmit(): Flow<Duration> {
        return flow {
            var lastTimeEmit = System.currentTimeMillis()
            while (true) {
                delay(200)
                val currentTimeMillis = System.currentTimeMillis()
                val elapsedTime = currentTimeMillis - lastTimeEmit
                emit(elapsedTime.milliseconds)
                lastTimeEmit = currentTimeMillis
            }

        }
    }
}