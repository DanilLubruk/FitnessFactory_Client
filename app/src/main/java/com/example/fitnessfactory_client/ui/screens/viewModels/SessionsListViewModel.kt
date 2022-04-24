package com.example.fitnessfactory_client.ui.screens.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.managers.CoachesAccessManager
import com.example.fitnessfactory_client.ui.screens.mySessionsScreen.SessionViewsListState
import com.example.fitnessfactory_client.ui.uiState.UsersListState
import com.example.fitnessfactory_client.utils.GuiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

open class SessionsListViewModel
@Inject constructor(
    private val coachesAccessManager: CoachesAccessManager
) : ViewModel() {

    protected val mutableSessionsListState =
        MutableStateFlow<SessionViewsListState>(SessionViewsListState.Loading)
    val sessionViewsListState: StateFlow<SessionViewsListState> = mutableSessionsListState

    private val mutableCoachesListState = MutableSharedFlow<UsersListState>()
    val coachesListState: SharedFlow<UsersListState> = mutableCoachesListState

    fun fetchCoachUsers(coachesUsersIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val coaches =
                    coachesAccessManager.getCoachesUsersByEmails(coachesUsersIds = coachesUsersIds)
                mutableCoachesListState.emit(UsersListState.Loaded(coaches))
            } catch (throwable: Exception) {
                throwable.printStackTrace()
                GuiUtils.showMessage(throwable.localizedMessage)
                mutableCoachesListState.emit(UsersListState.Error(throwable = throwable))
            }
        }
    }
}