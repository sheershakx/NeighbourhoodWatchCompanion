package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.home


data class HomeState(
    val openIncidentForm: Boolean ?=null
)

sealed class HomeEvent{
    object GetNeighbourhoodNameEvent: HomeEvent()
    object IncidentFormButtonClicked: HomeEvent()
}


