package com.srg.neighbourhoodwatchcompanion.presenter.ui.incident

import android.net.Uri


data class IncidentFormState(
    val incidentFormSavedSuccessful: Boolean? = null,
    val imageUploadSuccessful: Boolean?=null
)

sealed class IncidentFormEvent {
    object SubmitIncident : IncidentFormEvent()
    object OpenImagePicker: IncidentFormEvent()
    data class AddImageToPreview(val uri: Uri): IncidentFormEvent()
    data class RemoveImageFromPreview(val uri: Uri): IncidentFormEvent()
}