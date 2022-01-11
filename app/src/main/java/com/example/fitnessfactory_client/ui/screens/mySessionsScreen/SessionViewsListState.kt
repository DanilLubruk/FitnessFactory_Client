package com.example.fitnessfactory_client.ui.screens.mySessionsScreen

import com.example.fitnessfactory_client.data.views.SessionView

sealed class SessionViewsListState {
    data class Loaded(val sessionsList: List<SessionView>): SessionViewsListState()
    data class Error(val throwable: Throwable): SessionViewsListState()
    object Loading : SessionViewsListState()
}
