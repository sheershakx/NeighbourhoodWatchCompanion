package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.setting.account

import androidx.lifecycle.viewModelScope
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.MviViewModel
import com.srg.neighbourhoodwatchcompanion.data.dataStore.DataStoreRepo
import com.srg.neighbourhoodwatchcompanion.data.model.UserInfo
import com.srg.neighbourhoodwatchcompanion.domain.usecase.user.UpdateUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountEditViewModel @Inject constructor(
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val dataStoreRepo: DataStoreRepo
) :
    MviViewModel<BaseViewState<AccountEditState>, AccountEditEvents>() {
    override fun onTriggerEvent(eventType: AccountEditEvents) {
        when (eventType) {
            is AccountEditEvents.SubmitChanges -> {
                safeLaunch {
                    execute(
                        updateUserInfoUseCase(
                            UserInfo(
                                firstName = _firstName.value,
                                lastName = _lastName.value,
                                mobile = _mobile.value
                            )
                        )
                    ) {
                        setState(BaseViewState.Data(AccountEditState(true)))
                    }
                }
            }
        }
    }


    private var _firstName = MutableStateFlow<String>("")
    val firstName: StateFlow<String> get() = _firstName

    private var _lastName = MutableStateFlow<String>("")
    val lastName: StateFlow<String> get() = _lastName

    private var _email = MutableStateFlow<String>("")
    val email: StateFlow<String> get() = _email

    private var _mobile = MutableStateFlow<String>("")
    val mobile: StateFlow<String> get() = _mobile

    fun areInputsValid() =
        combine(firstName, lastName, email, mobile) { fN, lN, e, m ->
            if (fN.isEmpty() || lN.isEmpty() || e.isEmpty() || m.isEmpty())
                return@combine false
            else true
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), false)


    init {
        safeLaunch {
            launch {
                dataStoreRepo.firstName.collect {
                    _firstName.emit(it.toString())
                }
            }
            launch {
                dataStoreRepo.lastName.collect {
                    _lastName.emit(it.toString())
                }
            }
            launch {
                dataStoreRepo.email.collectLatest {
                    _email.emit(it.toString())
                }
            }
            launch {
                dataStoreRepo.mobile.collectLatest {
                    _mobile.emit(it.toString())
                }
            }
        }
    }

    fun updateFirstName(firstName: String) {
        _firstName.value = firstName
    }

    fun updateLastName(lastName: String) {
        _lastName.value = lastName
    }

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updateMobile(mobile: String) {
        _mobile.value = mobile
    }
}

