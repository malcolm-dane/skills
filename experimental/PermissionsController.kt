package dev.mdane.skills.experimental

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.skills.experimental.Permission


 interface PermissionsController {
   suspend fun providePermission(permission: Permission)
   fun isPermissionGranted(permission: Permission): Boolean

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
    }}




