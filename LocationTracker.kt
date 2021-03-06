package dev.mdane.skills.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.skills.Altitude
import com.example.skills.experimental.Permission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dev.mdane.skills.experimental.Permission
import dev.mdane.skills.experimental.PermissionsController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

actual class LocationTracker(
    actual val permissionsController: PermissionsController,
    interval: Long = 1000,
    priority: Int = LocationRequest.PRIORITY_HIGH_ACCURACY
) : LocationCallback() {
    private var locationProviderClient: FusedLocationProviderClient? = null
    private var isStarted: Boolean = false
    private val locationRequest = LocationRequest().also {
        it.interval = interval
        it.priority = priority
    }
    private val locationsChannel = Channel<LatLng>(Channel.BUFFERED)
    private val extendedLocationsChannel = Channel<ExtendedLocation>(Channel.BUFFERED)
    private val trackerScope = CoroutineScope(Dispatchers.Main)

    fun bind(lifecycle: Lifecycle, context: Context, fragmentManager: FragmentManager) {
        permissionsController.bind(lifecycle, fragmentManager)

        locationProviderClient = FusedLocationProviderClient(context)

        if (isStarted) {
            locationProviderClient?.requestLocationUpdates(locationRequest, this, null)
        }

        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                locationProviderClient?.removeLocationUpdates(this@LocationTracker)
                locationProviderClient = null
            }
        })
    }

    override fun onLocationResult(locationResult: LocationResult) {
        super.onLocationResult(locationResult)

        val lastLocation = locationResult.lastLocation

        val speedAccuracy = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) null
        else lastLocation.speedAccuracyMetersPerSecond.toDouble()

        val bearingAccuracy = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) null
        else lastLocation.bearingAccuracyDegrees.toDouble()

        val verticalAccuracy = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) null
        else lastLocation.verticalAccuracyMeters.toDouble()

        val latLng = LatLng(
            lastLocation.latitude,
            lastLocation.longitude
        )

        val locationPoint = Location(
            coordinates = latLng,
            coordinatesAccuracyMeters = lastLocation.accuracy.toDouble()
        )

        val speed = Speed(
            speedMps = lastLocation.speed.toDouble(),
            speedAccuracyMps = speedAccuracy
        )

        val azimuth = Azimuth(
            azimuthDegrees = lastLocation.bearing.toDouble(),
            azimuthAccuracyDegrees = bearingAccuracy
        )

        val altitude = Altitude(
            altitudeMeters = lastLocation.altitude,
            altitudeAccuracyMeters = verticalAccuracy
        )

        val extendedLocation = ExtendedLocation(
            location = locationPoint,
            azimuth = azimuth,
            speed = speed,
            altitude = altitude,
            timestampMs = lastLocation.time
        )

        trackerScope.launch {
            extendedLocationsChannel.send(extendedLocation)
            locationsChannel.send(latLng)
        }
    }

    @SuppressLint("MissingPermission")
    actual suspend fun startTracking() {
        permissionsController.providePermission(Permission.LOCATION)
        // if permissions request failed - execution stops here

        isStarted = true
        locationProviderClient?.requestLocationUpdates(locationRequest, this, null)
    }

    actual fun stopTracking() {
        isStarted = false
        locationProviderClient?.removeLocationUpdates(this)
    }

    actual fun getLocationsFlow(): Flow<LatLng> {
        return channelFlow {
            val sendChannel = channel
            val job = launch {
                while (isActive) {
                    val latLng = locationsChannel.receive()
                    sendChannel.send(latLng)
                }
            }

            awaitClose { job.cancel() }
        }
    }

    actual fun getExtendedLocationsFlow(): Flow<ExtendedLocation> {
        return channelFlow {
            val sendChannel = channel
            val job = launch {
                while (isActive) {
                    val extendedLocation = extendedLocationsChannel.receive()
                    sendChannel.send(extendedLocation)
                }
            }

            awaitClose { job.cancel() }
        }
    }
}
