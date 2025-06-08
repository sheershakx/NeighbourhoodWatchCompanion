package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.home

import androidx.lifecycle.viewModelScope
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.MviViewModel
import com.srg.neighbourhoodwatchcompanion.data.dataStore.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val dataStoreRepo: DataStoreRepo
) : MviViewModel<BaseViewState<HomeState>, HomeEvent>() {

    val userName =
        dataStoreRepo.firstName.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), false)

    val profileImage =
        dataStoreRepo.profileImage.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3000),
            false
        )

    override fun onTriggerEvent(eventType: HomeEvent) {
        when (eventType) {
            HomeEvent.IncidentFormButtonClicked -> {
                setState(BaseViewState.Data(HomeState(openIncidentForm = true)))
            }

            else -> {}
        }
    }

}

