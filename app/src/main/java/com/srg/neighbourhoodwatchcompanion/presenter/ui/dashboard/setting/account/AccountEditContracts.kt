package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.setting.account

import android.net.Uri


data class AccountEditState(
    val updateSuccessful: Boolean? = null,
)

sealed class AccountEditEvents {
    data class PreviewProfileImage(val uri: Uri) : AccountEditEvents()
    object SubmitChanges : AccountEditEvents()
}