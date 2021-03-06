package com.example.fitnessfactory_client.ui.screens.mySessionsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfactory_client.data.dataListeners.DaysUsersSessionsListListener
import com.example.fitnessfactory_client.data.managers.CoachesAccessManager
import com.example.fitnessfactory_client.data.managers.SessionsDataManager
import com.example.fitnessfactory_client.data.repositories.SessionViewRepository
import com.example.fitnessfactory_client.data.system.FirebaseAuthManager
import com.example.fitnessfactory_client.ui.screens.viewModels.SessionsListViewModel
import com.example.fitnessfactory_client.ui.uiState.UsersListState
import com.example.fitnessfactory_client.utils.GuiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class MySessionsListScreenViewModel
@Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    private val sessionViewRepository: SessionViewRepository,
    private val daysUsersSessionsListListener: DaysUsersSessionsListListener,
    private val sessionsDataManager: SessionsDataManager,
    private val coachesAccessManager: CoachesAccessManager
) : SessionsListViewModel(coachesAccessManager = coachesAccessManager) {

    fun startDataListener(date: Date) {
        viewModelScope.launch {
            firebaseAuthManager.getCurrentUserEmail()?.let { usersEmail ->
                daysUsersSessionsListListener.startDataListener(date = date, usersEmail = usersEmail)
                    .map { sessions ->
                        sessionViewRepository.getSessionViewsList(sessionsList = sessions)
                    }
                    .flowOn(Dispatchers.IO)
                    .catch { throwable ->
                        throwable.printStackTrace()
                        GuiUtils.showMessage(throwable.localizedMessage)
                        mutableSessionsListState.emit(SessionViewsListState.Error(throwable = throwable))
                    }
                    .collect { sessionViews ->
                        mutableSessionsListState.emit(SessionViewsListState.Loaded(sessionViews))
                    }
            }
        }
    }

    fun unsubscribeFromSession(sessionId: String) {
        viewModelScope.launch {
            sessionsDataManager.removeClientFromSession(sessionId = sessionId)
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                }
                .collect()
        }
    }
}