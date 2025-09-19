package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.setting

import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.MviViewModel
import com.srg.neighbourhoodwatchcompanion.domain.usecase.auth.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase
) : MviViewModel<BaseViewState<SettingState>, SettingEvent>() {

    override fun onTriggerEvent(eventType: SettingEvent) {
        when (eventType) {
            is SettingEvent.SignOutEvent -> {
                safeLaunch {
                    signOutUseCase(Unit)
                    setState(BaseViewState.Data(SettingState(navigateToLoginScreen = true)))
                }
            }
        }
    }

}

