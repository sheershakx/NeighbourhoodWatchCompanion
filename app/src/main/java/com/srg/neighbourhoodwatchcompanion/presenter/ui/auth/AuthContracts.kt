package com.srg.neighbourhoodwatchcompanion.presenter.ui.auth

data class AuthState(
    val isLoggedIn: Boolean
)


sealed class AuthEvent{
    object Login: AuthEvent()
}