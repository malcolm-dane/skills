/*
 * Skills
 */  
package dev.mdane.skills.location

import dev.mdane.skills.experimental.Parcelable
import dev.mdane.skills.experimental.Parcelize

@Parcelize
data class Altitude(
    val altitudeMeters: Double,
    val altitudeAccuracyMeters: Double?
) : Parcelable
