package dev.mdane.skills.location

import dev.mdane.skills.experimental.Parcelable
import dev.mdane.skills.experimental.Parcelize

@Parcelize
data class ExtendedLocation(
    val location: Location,
    val azimuth: Azimuth,
    val speed: Speed,
    val altitude: Altitude,
    val timestampMs: Long
) : Parcelable
