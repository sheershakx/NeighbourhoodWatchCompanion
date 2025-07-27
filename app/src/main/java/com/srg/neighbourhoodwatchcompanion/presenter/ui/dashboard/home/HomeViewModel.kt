package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.home

import androidx.lifecycle.viewModelScope
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.MviViewModel
import com.srg.neighbourhoodwatchcompanion.data.dataStore.DataStoreRepo
import com.srg.neighbourhoodwatchcompanion.data.model.GetDetailedIncident
import com.srg.neighbourhoodwatchcompanion.domain.usecase.incident.GetDetailedIncidentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val dataStoreRepo: DataStoreRepo,
    val getDetailedIncidentsUseCase: GetDetailedIncidentsUseCase
) : MviViewModel<BaseViewState<HomeState>, HomeEvent>() {

    val userName =
        dataStoreRepo.firstName.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), false)

    val profileImage =
        dataStoreRepo.profileImage.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3000),
            false
        )

    private var _detailedIncidents = MutableStateFlow<List<GetDetailedIncident>>(emptyList())
    val detailedIncidents: StateFlow<List<GetDetailedIncident>> get() = _detailedIncidents

    init {
        safeLaunch {
            execute(getDetailedIncidentsUseCase(Unit)) {
                _detailedIncidents.tryEmit(it)
            }
        }
    }

    override fun onTriggerEvent(eventType: HomeEvent) {
        when (eventType) {
            HomeEvent.IncidentFormButtonClicked -> {
                setState(BaseViewState.Data(HomeState(openIncidentForm = true)))
            }

            else -> {}
        }
    }

}

