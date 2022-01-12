package com.example.fitnessfactory_client.ui.screens.homeScreen

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.data.beans.GymsChainData
import com.example.fitnessfactory_client.data.dataListeners.DaysSessionsListListener
import com.example.fitnessfactory_client.data.dataListeners.SessionsCalendarListListener
import com.example.fitnessfactory_client.data.managers.CoachesAccessManager
import com.example.fitnessfactory_client.data.managers.GymsChainDataManager
import com.example.fitnessfactory_client.data.managers.SessionsDataManager
import com.example.fitnessfactory_client.data.repositories.SessionViewRepository
import com.example.fitnessfactory_client.ui.screens.mySessionsScreen.SessionViewsListState
import com.example.fitnessfactory_client.ui.screens.viewModels.SessionsListViewModel
import com.example.fitnessfactory_client.utils.GuiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class HomeScreenViewModel @Inject constructor(
    private val sessionsDataManager: SessionsDataManager,
    private val sessionViewRepository: SessionViewRepository,
    private val daysSessionsListListener: DaysSessionsListListener,
    private val sessionsCalendarListListener: SessionsCalendarListListener,
    private val coachesAccessManager: CoachesAccessManager,
    private val gymsChainDataManager: GymsChainDataManager
) : SessionsListViewModel(coachesAccessManager = coachesAccessManager) {

    private val mutableCalendarSessionsListState =
        MutableStateFlow<SessionsListState>(SessionsListState.Loading)
    val calendarSessionsListState: StateFlow<SessionsListState> = mutableCalendarSessionsListState

    private val mutableGymsChainDataState = MutableSharedFlow<GymsChainData>()
    val gymsChainDataState: SharedFlow<GymsChainData> = mutableGymsChainDataState

    fun startCalendarSessionsDataListener(startDate: Date, endDate: Date) {
        viewModelScope.launch {
            sessionsCalendarListListener.startDataListener(startDate = startDate, endDate = endDate)
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                    mutableCalendarSessionsListState.emit(SessionsListState.Error(throwable))
                }
                .collect {
                    mutableCalendarSessionsListState.emit(SessionsListState.Loaded(it))
                }
        }
    }

    fun startSessionViewsDataListener(date: Date) {
        viewModelScope.launch {
            daysSessionsListListener.startDataListener(date = date)
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

    fun subscribeToSession(sessionId: String) {
        viewModelScope.launch {
            sessionsDataManager.addClientToSession(sessionId = sessionId)
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                }
                .collect()
        }
    }

    fun fetchGymsChainData() {
        viewModelScope.launch {
            gymsChainDataManager.getGymsChainData()
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    throwable.printStackTrace()
                    GuiUtils.showMessage(throwable.localizedMessage)
                }
                .collect { gymsChainData ->
                    mutableGymsChainDataState.emit(gymsChainData)
                }
        }
    }
}