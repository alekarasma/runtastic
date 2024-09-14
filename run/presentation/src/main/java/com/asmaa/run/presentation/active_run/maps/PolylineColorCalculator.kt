package com.asmaa.run.presentation.active_run.maps

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.asmaa.core.domain.location.LocationTimeStamp
import kotlin.math.abs


object PolylineColorCalculator {
    fun locationsToColor(l1: LocationTimeStamp, l2: LocationTimeStamp): Color {
        val distanceInMeters = l1.location.location.distanceTo(l2.location.location)
        val timeDiff = abs((l2.durationTimestamp - l1.durationTimestamp).inWholeSeconds)
        val speedKmh = (distanceInMeters / timeDiff) * 3.6
        return interpolateColor(speedKmh, 5.0, 20.0, Color.Green, Color.Red, Color.Yellow)
    }

    private fun interpolateColor(
        speedKmh: Double,
        minSpeed: Double,
        maxSpeed: Double,
        colorStart: Color,
        colorEnd: Color,
        colorMid: Color
    ): Color {
        val ratio = ((speedKmh - minSpeed) / (maxSpeed - minSpeed)).coerceIn(0.0..1.0)
        val colorInt = if (ratio <= 0.5) {
            val midRatio = ratio / 0.5
            ColorUtils.blendARGB(colorStart.toArgb(), colorMid.toArgb(), midRatio.toFloat())
        } else {
            val midToEndRatio = (ratio - 0.5) / 0.5
            ColorUtils.blendARGB(colorMid.toArgb(), colorEnd.toArgb(), midToEndRatio.toFloat())
        }
        return Color(colorInt)
    }
}