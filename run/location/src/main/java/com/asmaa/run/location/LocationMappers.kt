package com.asmaa.run.location

import android.location.Location
import com.asmaa.core.domain.location.LocationWithAltitude

fun Location.toLocationWithAltitude(): LocationWithAltitude {
    return LocationWithAltitude(
        location = com.asmaa.core.domain.location.Location(lat = latitude, long = longitude),
        altitude = altitude
    )
}