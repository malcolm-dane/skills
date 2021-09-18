package dev.mdane.skills.experimental
import dev.mdane.skills.experimental.PermissionsController
import kotlinx.coroutines.flow.Flow

expect class LocationTracker {
    val permissionsController: PermissionsController

    suspend fun startTracking() // can be suspended for request permission
    fun stopTracking()

    fun getLocationsFlow(): Flow<LatLng>

    fun getExtendedLocationsFlow(): Flow<ExtendedLocation>
}
