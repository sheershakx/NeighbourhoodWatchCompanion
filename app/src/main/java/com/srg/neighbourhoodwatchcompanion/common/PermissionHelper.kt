package com.srg.neighbourhoodwatchcompanion.common

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.srg.neighbourhoodwatchcompanion.presenter.ui.incident.openAppSettings


object PermissionHelper {

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