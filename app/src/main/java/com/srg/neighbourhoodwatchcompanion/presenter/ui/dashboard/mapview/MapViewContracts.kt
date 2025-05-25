package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.mapview


data class MapViewState(
    val isLoading: Boolean = false,
)

sealed class MapViewEvent {
    object TestEvent : MapViewEvent()
}

data class IncidentCardDataModel(
    val incidentType: String,
    val location: String,
    val dateTime: String,
    val casualties: String
)