package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.mapview

import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.MviViewModel
import com.srg.neighbourhoodwatchcompanion.data.model.GetDetailedIncident
import com.srg.neighbourhoodwatchcompanion.domain.usecase.incident.GetDetailedIncidentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewViewModel @Inject constructor(
    private val getDetailedIncidentsUseCase: GetDetailedIncidentsUseCase
) :
    MviViewModel<BaseViewState<MapViewState>, MapViewEvent>() {
    override fun onTriggerEvent(eventType: MapViewEvent) {
        when (eventType) {
            is MapViewEvent.MapMarkerClicked -> {
                setState(BaseViewState.Data(MapViewState(scrollPageTo = eventType.page)))
            }
        }
    }

    private var _detailedIncidents = MutableStateFlow<List<GetDetailedIncident>>(emptyList())
    val detailedIncidents: StateFlow<List<GetDetailedIncident>> get() = _detailedIncidents

    init {
        safeLaunch {
            execute(getDetailedIncidentsUseCase(Unit)) {
                _detailedIncidents.tryEmit(it)
            }
        }
    }

}