package com.srg.neighbourhoodwatchcompanion.presenter.ui.incident


data class IncidentFormState(
    val nothing: String? = null,
    val incidentFormSavedSuccessful: Boolean? = null,
)

sealed class IncidentFormEvent {
    object SubmitIncident : IncidentFormEvent()
}