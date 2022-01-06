package com.example.fitnessfactory_client.ui.screens.splashScreen

sealed class IsAuthState {
    class LoggedIn: IsAuthState()
    class LoggedOut: IsAuthState()
    data class Error(val throwable: Throwable): IsAuthState()
}
