package com.asmaa.core.database.mappers

import com.asmaa.core.database.enitity.RunEntity
import com.asmaa.core.domain.run.Run
import com.asmaa.core.domain.location.Location
import org.bson.types.ObjectId
import java.time.ZoneId
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

fun RunEntity.toRun(): Run {
    return Run(
        id = id,
        duration = durationMillis.milliseconds,
        dateTimeUtc = Instant.parse(dateTimeUtc)
            .atZone(ZoneId.of("UTC")),
        distanceMeters = distanceMeters,
        location = Location(
            lat = lat,
            long = long
        ),
        maxSpeed = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl
    )
}

fun Run.toRunEntity(): RunEntity {
    return RunEntity(
        id = id ?: ObjectId().toHexString(),
        durationMillis = duration.inWholeMilliseconds,
        maxSpeedKmh = maxSpeed,
        dateTimeUtc = dateTimeUtc.toInstant().toString(),
        lat = location.lat,
        long = location.long,
        distanceMeters = distanceMeters,
        avgSpeedKmh = avgSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl
    )
}