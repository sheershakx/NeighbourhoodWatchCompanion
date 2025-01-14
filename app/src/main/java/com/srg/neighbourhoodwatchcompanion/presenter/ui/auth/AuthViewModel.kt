package com.srg.neighbourhoodwatchcompanion.presenter.ui.auth

import android.util.Patterns
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.MviViewModel
import com.srg.framework.extension.cast
import com.srg.framework.extension.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject
import com.srg.neighbourhoodwatchcompanion.common.StringResources as SR


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val supabaseAuth: Auth,
) : MviViewModel<BaseViewState<AuthState>, AuthEvent>() {

    companion object {
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val CONFIRM_PASSWORD = "confirm_password"
    }



    init {
        safeLaunch {
            observeAuthSessions()
        }
    }


    override fun onTriggerEvent(eventType: AuthEvent) {
        when (eventType) {
            is AuthEvent.Login -> {
                safeLaunch {
                    setState(BaseViewState.Loading)
                    supabaseAuth.signInWith(Email) {
                        email = emailValue.value.first
                        password = passwordValue.value.first
                    }
                }
                // do login event process
            }

            is AuthEvent.Register -> {
                safeLaunch {
                    setState(BaseViewState.Loading)
                    supabaseAuth.signUpWith(Email) {
                        email = emailValue.value.first
                        password = passwordValue.value.first
                    }
                }
            }
        }
    }


    //first string for value and second for the error message
    var emailValue = savedStateHandle.getStateFlow(EMAIL, Pair("", ""))
    val passwordValue = savedStateHandle.getStateFlow(PASSWORD, Pair("", ""))
    val confirmPasswordValue = savedStateHandle.getStateFlow(CONFIRM_PASSWORD, Pair("", ""))

    private val _currentScreen= MutableStateFlow(AuthScreen.LOGIN_SCREEN)
    val currentScreen:StateFlow<AuthScreen> get() = _currentScreen

    fun setCurrentScreen(authScreen: AuthScreen){
        _currentScreen.value=authScreen
    }


    private suspend fun observeAuthSessions() {
        supabaseAuth.sessionStatus.collect {
            when (it) {
                is SessionStatus.Authenticated -> {
                    when (it.source) { //Check the source of the session
                        is SessionSource.SignUp -> {
                            setState(BaseViewState.Data(AuthState(userRegistrationState = it.session.user)))
                            //collect data and redirect to screen
                        }

                        is SessionSource.SignIn -> {
                            setState(BaseViewState.Data(AuthState(isUserLoggedIn = true)))
                            //listen upon sign in
                        }


                        else -> {
                            Timber.d("Session Source ${it.source}")
                        }
                    }
                }

                SessionStatus.Initializing -> Timber.d("Initializing")
                is SessionStatus.RefreshFailure -> Timber.d("Refresh failure ${it.cause}") //Either a network error or a internal server error
                is SessionStatus.NotAuthenticated -> {
                    if (it.isSignOut) {
                        Timber.d("User signed out")
                    } else {
                        Timber.d("User not signed in")
                    }
                }

            }
        }

    }

    fun isRegistrationFormValid() =
        combine(
            emailValue,
            passwordValue,
            confirmPasswordValue
        ) { email, password, confirmPassword ->
            if (emailValue.value.first.isEmpty() || passwordValue.value.first.isEmpty() || confirmPasswordValue.value.first.isEmpty()) return@combine false
            email.second.isEmpty() && password.second.isEmpty() && confirmPassword.second.isEmpty()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), false)

    fun isLoginFormValid() =
        combine(
            emailValue,
            passwordValue,
        ) { email, password ->
            if (emailValue.value.first.isEmpty() || passwordValue.value.first.isEmpty()) return@combine false
            email.second.isEmpty() && password.second.isEmpty()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), false)

    /**
     * Input validator functions
     * */
    fun emailValidator(email: String): String {
        Timber.d("CURRENT STATE= $currentScreen")
        return if (email.isEmpty()) return SR.EMPTY_EMAIL
        else if (currentScreen.value != AuthScreen.LOGIN_SCREEN  && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) SR.INCORRECT_EMAIL_FORMAT
        else ""
    }


    fun passwordValidator(password: String): String {
        return if (password.isEmpty()) SR.EMPTY_PASSWORD
        else if (currentScreen.value== AuthScreen.LOGIN_SCREEN) ""
        else if (password.length < 8) SR.SHORT_PASSWORD
//        else if (!password.matches(".*[A-Z].*".toRegex())) "uppercase"  // no need right now
        else if (!password.matches(".*[@#\$%^&+=].*".toRegex())) SR.SPECIAL_CHARACTER_PASSWORD
//        else if (!password.matches(".*[0-9].*".toRegex())) "number" // no need right now
        else ""
    }

    fun confirmPasswordValidator(confirmPassword: String): String {
        return if (confirmPassword.isEmpty()) SR.EMPTY_PASSWORD
        else if (confirmPassword != passwordValue.value.first) SR.MISMATCH_PASSWORD
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



    fun clearState(){
        setState(BaseViewState.Empty)
    }
}
