package com.example.fitnessfactory_client.ui.screens.homeScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.GymsChainData
import com.example.fitnessfactory_client.data.beans.PeriodObject
import com.example.fitnessfactory_client.data.beans.SessionsFilter
import com.example.fitnessfactory_client.data.beans.TopBarAction
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.ui.components.FilterScreen
import com.example.fitnessfactory_client.ui.components.HomeScreenCalendarView
import com.example.fitnessfactory_client.ui.components.SessionsListView
import com.example.fitnessfactory_client.ui.components.TopBar
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.collections.ArrayList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.fitnessfactory_client.utils.DialogUtils.YesNoDialog

object HomeScreen {

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun HomeScreen(
        lifecycle: Lifecycle,
        sessionsFilter: SessionsFilter = SessionsFilter.getNoFilterEntity(),
        openDrawer: () -> Unit,
        setFilter: (SessionsFilter) -> Unit,
        clearFilter: () -> Unit,
        logout: () -> Unit,
    ) {
        var showLogoutDialog by remember { mutableStateOf(false) }
        BackHandler {
            showLogoutDialog = true
        }
        if (showLogoutDialog) {
            YesNoDialog(
                onOkPress = {
                    showLogoutDialog = false
                    logout()
                },
                onDismissRequest = {
                    showLogoutDialog = false
                },
                questionText = stringResource(id = R.string.message_ask_logout)
            )
        }

        val viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModelFactory())

        var date by rememberSaveable { mutableStateOf(Date()) }
        var showSessionsList by rememberSaveable { mutableStateOf(false) }
        val setListenerDate: (Date) -> Unit = {
            if (!showSessionsList) {
                showSessionsList = true
            }
            date = it
        }

        var calendarListenerPeriod by remember { mutableStateOf(PeriodObject()) }
        LaunchedEffect(calendarListenerPeriod, sessionsFilter) {
            viewModel.startCalendarSessionsDataListener(
                startDate = calendarListenerPeriod.startDate,
                endDate = calendarListenerPeriod.endDate,
                sessionsFilter = sessionsFilter
            )
        }

        val setCalendarListenerDates: (Date, Date) -> Unit = { startDate, endDate ->
            calendarListenerPeriod = PeriodObject(startDate = startDate, endDate = endDate)
        }
        val subscribeToSession: (String) -> Unit = { sessionId ->
            viewModel.subscribeToSession(sessionId = sessionId)
        }

        var showFilterDialog by remember { mutableStateOf(false) }
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar.TopBar(
                title = ResUtils.getString(R.string.title_home_screen),
                buttonIcon = Icons.Filled.Menu,
                onButtonClicked = { openDrawer() },
                actions = listOf(
                    TopBarAction(stringResource(id = R.string.caption_filter)) { viewModel.fetchGymsChainData() },
                    TopBarAction(stringResource(id = R.string.caption_clear)) { clearFilter() }
                )
            )

            var gymsChainData: GymsChainData by remember { mutableStateOf(GymsChainData.getBlankObject()) }
            LaunchedEffect(key1 = Unit) {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.gymsChainDataState.collect { chainData ->
                        gymsChainData = chainData
                        showFilterDialog = true
                    }
                }
            }
            if (showFilterDialog) {
                FilterScreen.FilterScreen(
                    onDismissRequest = { showFilterDialog = false },
                    sessionsFilter = sessionsFilter,
                    chainData = gymsChainData,
                    setFilter = {
                        setFilter(it)
                    }
                )
            }

            var sessionsList by remember { mutableStateOf(ArrayList<Session>()) }
            LaunchedEffect(key1 = Unit) {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.calendarSessionsListState.collect { calendarListState ->
                        when (calendarListState) {
                            is SessionsListState.Loaded -> {
                                sessionsList = calendarListState.sessionsList
                            }
                            is SessionsListState.Error -> {}
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HomeScreenCalendarView.HomeScreenCalendarView(
                    sessionsList = sessionsList,
                    setListenerDate = setListenerDate,
                    setCalendarListenerDates = setCalendarListenerDates
                )
            }

            if (showSessionsList) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SessionsListView.SessionsListViewScreen(
                        lifecycle = lifecycle,
                        date = date,
                        listStateFlow = viewModel.sessionViewsListState,
                        startDataListener = { listenerDate ->
                            viewModel.startSessionViewsDataListener(
                                date = listenerDate
                            )
                        },
                        onItemClickAction = subscribeToSession,
                        onItemActionName = ResUtils.getString(R.string.caption_subscribe),
                        askActionMessage = StringUtils.getMessageSubscribeToSession(),
                        fetchCoachUsers = { coachesIds -> viewModel.fetchCoachUsers(coachesIds = coachesIds) },
                        coachUsersFlow = viewModel.coachesListState
                    )
                }
            }
        }
    }
}