package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.home

import androidx.lifecycle.viewModelScope
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.MviViewModel
import com.srg.neighbourhoodwatchcompanion.data.dataStore.DataStoreRepo
import com.srg.neighbourhoodwatchcompanion.domain.usecase.home.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val dataStoreRepo: DataStoreRepo
) : MviViewModel<BaseViewState<HomeState>, HomeEvent>() {

    val userName= dataStoreRepo.firstName.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000),false)

    //TODO :: user info not updating instantly on home page as well as account screen after doing update from account screen

    init {
        safeLaunch {
            execute(getUserInfoUseCase(Unit), false)

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

