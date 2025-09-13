package com.srg.neighbourhoodwatchcompanion.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.srg.neighbourhoodwatchcompanion.data.model.LatLngData
import com.srg.neighbourhoodwatchcompanion.presenter.ui.incident.openAppSettings
import timber.log.Timber


object PermissionHelper {

    @OptIn(ExperimentalPermissionsApi::class)
    fun handleLocationPermissionRequest(
        locationPermissionState: MultiplePermissionsState,
        locationPermissionRequested: Boolean,
        setLocationPermissionRequested: (Boolean) -> Unit,
        context: Context,
        onLocationReceived: (LatLngData) -> Unit
    ) {
        when (locationPermissionState.allPermissionsGranted) {
            true -> {
                context.getCurrentLocation(onLocationReceived)
            }

            false -> {
                if (locationPermissionRequested) {
                    if (locationPermissionState.shouldShowRationale) {
                        locationPermissionState.launchMultiplePermissionRequest()
                    } else {
                        (context as? Activity)?.openAppSettings()
                    }
                } else {
                    locationPermissionState.launchMultiplePermissionRequest()
                    setLocationPermissionRequested(true)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")   //safe as we check permission before calling this fn
    fun Context.getCurrentLocation(
        onLocationReceived: (LatLngData) -> Unit
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            if (location != null) {
                onLocationReceived(LatLngData(location.latitude, location.longitude))
            } else {
                Timber.e("Could not fetch location")
            }

        }

    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun handleImagePermissionAndLaunchPicker(
        imagePermissionState: PermissionState,
        imagePermissionRequested: Boolean,
        setImagePermissionRequested: (Boolean) -> Unit,
        activity: Activity?,
        imagePickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    ) {
        when (val status = imagePermissionState.status) {
            is PermissionStatus.Granted -> {
                imagePickerLauncher.launch(
                    PickVisualMediaRequest(
                        mediaType = ImageOnly,
                        maxItems = 3
                    )
                )
            }

            is PermissionStatus.Denied -> {
                if (imagePermissionRequested) {
                    if (status.shouldShowRationale) {
                        imagePermissionState.launchPermissionRequest()
                    } else {
                        activity?.openAppSettings()
                    }
                } else {
                    imagePermissionState.launchPermissionRequest()
                    setImagePermissionRequested(true)
                }
            }
        }
    }


}