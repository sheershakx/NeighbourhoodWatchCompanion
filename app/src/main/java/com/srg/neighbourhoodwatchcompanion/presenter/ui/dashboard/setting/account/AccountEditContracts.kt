package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.setting.account


data class AccountEditState(
    val updateSuccessful: Boolean?= null

)

sealed class AccountEditEvents {
    object  SubmitChanges : AccountEditEvents()
}