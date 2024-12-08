package com.asmaa.run.presentation.run_overview.mapper

import com.asmaa.core.domain.run.Run
import com.asmaa.core.presentation.ui.formatted
import com.asmaa.core.presentation.ui.toFormattedKm
import com.asmaa.core.presentation.ui.toFormattedKmh
import com.asmaa.core.presentation.ui.toFormattedMeters
import com.asmaa.core.presentation.ui.toFormattedPace
import com.asmaa.run.presentation.run_overview.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi {
    val dateTimeInLocalTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())
    val formatteDateTime = DateTimeFormatter
        .ofPattern("MM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTime)

    val distanceKm = distanceMeters / 1000.0
    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formatteDateTime,
        distance = distanceKm.toFormattedKm(),
        avgSpeed = avgSpeedKmh.toFormattedKmh(),
        maxSpeed = maxSpeed.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl
    )


}
