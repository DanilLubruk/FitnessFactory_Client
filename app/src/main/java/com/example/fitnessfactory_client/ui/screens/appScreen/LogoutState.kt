package com.example.fitnessfactory_client.ui.screens.appScreen

sealed class LogoutState {
    class Success: LogoutState()
    data class Failure(val message: String): LogoutState()
}
