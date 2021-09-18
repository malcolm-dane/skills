package dev.mdane.skills.location

import dev.mdane.skills.experimental.Parcelable
import dev.mdane.skills.experimental.Parcelize

@Parcelize
data class Speed(
    val speedMps: Double,
    val speedAccuracyMps: Double?
) : Parcelable
