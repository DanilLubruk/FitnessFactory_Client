package com.example.fitnessfactory_client.ui.screens.mySessionsScreen

import com.example.fitnessfactory_client.data.views.SessionView

sealed class SessionsListState {
    data class Loaded(val sessionsList: List<SessionView>): SessionsListState()
    data class Error(val throwable: Throwable): SessionsListState()
    object Loading : SessionsListState()
}
