package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.home

import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.MviViewModel
import com.srg.neighbourhoodwatchcompanion.data.model.UserInfo
import com.srg.neighbourhoodwatchcompanion.domain.usecase.home.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase
) : MviViewModel<BaseViewState<HomeState>, HomeEvent>() {

    private val _userInfo = MutableStateFlow<UserInfo>(UserInfo.empty)
    val userInfo = _userInfo.asStateFlow()


    init {
        safeLaunch {
            execute(getUserInfoUseCase(Unit), false) { data ->
                _userInfo.value = data
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

