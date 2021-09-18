package dev.mdane.skills.location

import dev.mdane.skills.experimental.Parcelable
import dev.mdane.skills.experimental.Parcelize

@Parcelize
class Location(
    val coordinates: LatLng,
    val coordinatesAccuracyMeters: Double
) : Parcelable
