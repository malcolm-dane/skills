package dev.mdane.skills.location

import dev.mdane.skills.experimental.Parcelable
import dev.mdane.skills.experimental.Parcelize

@Parcelize
data class Azimuth(
    val azimuthDegrees: Double,
    val azimuthAccuracyDegrees: Double?
) : Parcelable
