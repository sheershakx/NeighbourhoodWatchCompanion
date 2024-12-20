package com.srg.neighbourhoodwatchcompanion.presenter.ui.auth

import io.github.jan.supabase.auth.user.UserInfo

data class AuthState(
    val isInAuthScreen: AuthScreen?=null,
    val isUserLoggedIn: Boolean=false,
    val userRegistrationState: UserInfo?=null
)

enum class AuthScreen{
    LOGIN_SCREEN,
    REGISTER_SCREEN
}

sealed class AuthEvent{
    object Login: AuthEvent()
    object Register: AuthEvent()
}