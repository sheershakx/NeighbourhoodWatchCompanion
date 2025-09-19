package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.setting


data class SettingState(
    val navigateToLoginScreen: Boolean? = null
)

sealed class SettingEvent {
    object SignOutEvent : SettingEvent()
}