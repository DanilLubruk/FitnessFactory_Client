package com.example.fitnessfactory_client.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.SessionsFilter
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.data.views.SessionView
import com.example.fitnessfactory_client.ui.screens.mySessionsScreen.SessionViewsListState
import com.example.fitnessfactory_client.ui.uiState.ListState
import com.example.fitnessfactory_client.ui.uiState.UsersListState
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.TimeUtils
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import kotlin.collections.ArrayList

object SessionsListView {

    @ExperimentalMaterialApi
    @Composable
    fun SessionsListViewScreen(
        lifecycle: Lifecycle,
        date: Date,
        sessionsFilter: SessionsFilter,
        listStateFlow: StateFlow<SessionViewsListState>,
        startDataListener: (Date, SessionsFilter) -> Unit,
        fetchCoachUsers: (List<String>) -> Unit,
        coachUsersFlow: SharedFlow<UsersListState>,
        showBottomSheet: (SessionView, List<AppUser>) -> Unit,
    ) = SessionsListViewScreen(
        lifecycle = lifecycle,
        startDate = date,
        endDate = date,
        sessionsFilter = sessionsFilter,
        listStateFlow = listStateFlow,
        startDataListener = { startDate, endDate, newSessionsFilter -> startDataListener(startDate, newSessionsFilter) },
        fetchCoachUsers = fetchCoachUsers,
        coachUsersFlow = coachUsersFlow,
        showBottomSheet = showBottomSheet
    )

    @ExperimentalMaterialApi
    @Composable
    fun SessionsListViewScreen(
        lifecycle: Lifecycle,
        startDate: Date,
        endDate: Date,
        sessionsFilter: SessionsFilter,
        listStateFlow: StateFlow<SessionViewsListState>,
        startDataListener: (Date, Date, SessionsFilter) -> Unit,
        fetchCoachUsers: (List<String>) -> Unit,
        coachUsersFlow: SharedFlow<UsersListState>,
        showBottomSheet: (SessionView, List<AppUser>) -> Unit,
    ) {
        var sessionsList: List<SessionView> by remember { mutableStateOf(ArrayList()) }

        var listState: ListState by remember { mutableStateOf(ListState.Loading) }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                listStateFlow.collect { sessionsListState ->
                    when (sessionsListState) {
                        is SessionViewsListState.Loaded -> {
                            sessionsList = sessionsListState.sessionsList
                            if (sessionsListState.sessionsList.isEmpty()) {
                                listState = ListState.Empty
                            } else {
                                listState = ListState.Loaded
                            }
                        }
                        is SessionViewsListState.Error -> {
                            listState = ListState.Loaded
                        }
                        is SessionViewsListState.Loading -> listState = ListState.Loading
                    }
                }
            }
        }

        LaunchedEffect(startDate, endDate, sessionsFilter) {
            startDataListener(startDate, endDate, sessionsFilter)
        }

        when (listState) {
            is ListState.Loading -> ListLoadingView.ListLoadingView()
            is ListState.Empty -> ListEmptyView.ListEmptyView(stringResource(id = R.string.caption_no_sessions_for_date))
            is ListState.Loaded -> SessionsListView(
                lifecycle = lifecycle,
                sessionsList = sessionsList,
                fetchCoachUsers = fetchCoachUsers,
                coachUsersFlow = coachUsersFlow,
                showBottomSheet = showBottomSheet,
            )
        }
    }

    @ExperimentalMaterialApi
    @Composable
    private fun SessionsListView(
        lifecycle: Lifecycle,
        sessionsList: List<SessionView>,
        fetchCoachUsers: (List<String>) -> Unit,
        coachUsersFlow: SharedFlow<UsersListState>,
        showBottomSheet: (SessionView, List<AppUser>) -> Unit,
    ) {
        var sessionData: SessionView by remember { mutableStateOf(SessionView(Session())) }

        var coaches: List<AppUser> by remember { mutableStateOf(ArrayList()) }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                coachUsersFlow.collect { usersListState ->
                    when (usersListState) {
                        is UsersListState.Loaded -> {
                            coaches = usersListState.usersList
                            showBottomSheet(sessionData, coaches)
                        }
                        is UsersListState.Error -> {}
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            itemsIndexed(sessionsList) { index, item ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .pointerInput(item) {
                        detectTapGestures(
                            onPress = { },
                            onDoubleTap = { },
                            onLongPress = { },
                            onTap = {
                                sessionData = item
                                if (item.session.coachesEmails == null) {
                                    fetchCoachUsers(ArrayList<String>())
                                } else {
                                    fetchCoachUsers(item.session.coachesEmails!!)
                                }
                            }
                        )
                    }) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            color = Color.Gray,
                            text = item.session.dateString,
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.gymName,
                            color = Color.Black,
                            fontSize = 16.sp
                        )

                        Row {
                            Text(
                                text = ResUtils.getString(R.string.caption_from),
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                color = Color.Gray,
                                text = item.session.startTimeString,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            color = Color.Gray,
                            text = item.sessionTypeName,
                            fontSize = 14.sp
                        )

                        Row {
                            Text(
                                text = ResUtils.getString(R.string.caption_until),
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = item.session.endTimeString,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}