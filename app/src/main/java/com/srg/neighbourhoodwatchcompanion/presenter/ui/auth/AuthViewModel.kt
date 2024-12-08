package com.srg.neighbourhoodwatchcompanion.presenter.ui.auth

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.MviViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


class AuthViewModel(
    private val savedStateHandle: SavedStateHandle
) : MviViewModel<BaseViewState<AuthState>, AuthEvent>() {

    companion object {
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val CONFIRM_PASSWORD = "confirm_password"
    }

    override fun onTriggerEvent(eventType: AuthEvent) {
        when(eventType){
            is AuthEvent.Login -> {
                // do auth event process
                safeLaunch {
                    setState(BaseViewState.Loading)
                }
            }
        }
    }


    //first string for value and second for the error message
    var emailValue = savedStateHandle.getStateFlow(EMAIL, Pair("", ""))
    val passwordValue = savedStateHandle.getStateFlow(PASSWORD, Pair("", ""))
    val confirmPasswordValue = savedStateHandle.getStateFlow(CONFIRM_PASSWORD, Pair("", ""))


    fun isFormValid() =
        combine(
            emailValue,
            passwordValue,
            confirmPasswordValue
        ) { email, password, confirmPassword ->
            if (emailValue.value.first.isEmpty() || passwordValue.value.first.isEmpty() || confirmPasswordValue.value.first.isEmpty()) return@combine false
            email.second.isEmpty() && password.second.isEmpty() && confirmPassword.second.isEmpty()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), false)


    /**
     * Input validator functions
     * */
    fun emailValidator(email: String): String {
        return if (email.isEmpty()) return "empty"
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "email format"
        else ""
    }

    fun passwordValidator(password: String): String {
        return if (password.isEmpty()) "empty"
        else if (password.length < 8) "length"
        else if (!password.matches(".*[A-Z].*".toRegex())) "uppercase"
        else if (!password.matches(".*[@#\$%^&+=].*".toRegex())) "special character"
        else if (!password.matches(".*[0-9].*".toRegex())) "number"
        else ""
    }

    fun confirmPasswordValidator(confirmPassword: String): String {
        return if (confirmPassword.isEmpty()) "empty"
        else if (confirmPassword != passwordValue.value.first) "password doesn't match"
        else ""
    }

    /**
     * On Input changes, update functions
     * */

    fun updateEmail(email: String) {
        savedStateHandle[EMAIL] = emailValue.value.copy(email, emailValidator(email))
    }

    fun updatePassword(password: String) {
        savedStateHandle[PASSWORD] = passwordValue.value.copy(password, passwordValidator(password))
        confirmPasswordValue.value.first.let { it ->
            if (it.isNotEmpty())
                updateConfirmPassword(it)
        }
        if (confirmPasswordValue.value.first.isNotEmpty()) {
            updateConfirmPassword(confirmPasswordValue.value.first)
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        savedStateHandle[CONFIRM_PASSWORD] = confirmPasswordValue.value.copy(
            confirmPassword,
            confirmPasswordValidator(confirmPassword)
        )
    }
}
