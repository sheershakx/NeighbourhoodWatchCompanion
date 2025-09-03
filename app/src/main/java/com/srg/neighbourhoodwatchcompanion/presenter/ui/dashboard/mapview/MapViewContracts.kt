package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.mapview


data class MapViewState(
    val scrollPageTo: Int? = null,
    val isLoading: Boolean = false,
)

sealed class MapViewEvent {
    data class MapMarkerClicked(val page: Int) : MapViewEvent()
}
