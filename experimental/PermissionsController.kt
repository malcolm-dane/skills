package dev.mdane.skills.experimental

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

actual interface PermissionsController {
    actual suspend fun providePermission(permission: Permission)
    actual fun isPermissionGranted(permission: Permission): Boolean

    fun bind(lifecycle: Lifecycle, fragmentManager: FragmentManager)

    companion object {
        operator fun invoke(
            resolverFragmentTag: String = "PermissionsControllerResolver",
            applicationContext: Context
        ): PermissionsController {
            return PermissionsControllerImpl(
                resolverFragmentTag = resolverFragmentTag,
                applicationContext = applicationContext
            )
        }
    }
}
