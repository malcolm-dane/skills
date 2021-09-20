/*
 * Skills
 */  
package com.example.skills


import android.os.Parcel
import dev.mdane.skills.experimental.Parcelable
import dev.mdane.skills.experimental.Parcelize

@Parcelize
data class Altitude(
    val altitudeMeters: Double,
    val altitudeAccuracyMeters: Double?
) : android.os.Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(altitudeMeters)
        parcel.writeValue(altitudeAccuracyMeters)
    }

}
