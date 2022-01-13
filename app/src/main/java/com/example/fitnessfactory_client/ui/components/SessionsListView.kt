package com.example.fitnessfactory_client.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.data.views.SessionView
import com.example.fitnessfactory_client.ui.screens.mySessionsScreen.SessionViewsListState
import com.example.fitnessfactory_client.ui.uiState.ListState
import com.example.fitnessfactory_client.ui.uiState.UsersListState
import com.example.fitnessfactory_client.utils.DialogUtils
import com.example.fitnessfactory_client.utils.ResUtils
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
        listStateFlow: StateFlow<SessionViewsListState>,
        startDataListener: (Date) -> Unit,
        onItemClickAction: (String) -> Unit,
        onItemActionName: String,
        askActionMessage: String,
        fetchCoachUsers: (List<String>) -> Unit,
        coachUsersFlow: SharedFlow<UsersListState>
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

        LaunchedEffect(date) {
            startDataListener(date)
        }

        when (listState) {
            is ListState.Loading -> ListLoadingView()
            is ListState.Empty -> ListEmptyView()
            is ListState.Loaded -> SessionsListView(
                lifecycle = lifecycle,
                sessionsList = sessionsList,
                onItemClickAction = onItemClickAction,
                onItemActionName = onItemActionName,
                askActionMessage = askActionMessage,
                fetchCoachUsers = fetchCoachUsers,
                coachUsersFlow = coachUsersFlow
            )
        }
    }

    @Composable
    private fun ListLoadingView() {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colors.primary
            )
        }
    }

    @Composable
    private fun ListEmptyView() {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = ResUtils.getString(R.string.caption_no_sessions_for_date),
                style = MaterialTheme.typography.body1
            )
        }
    }

    @ExperimentalMaterialApi
    @Composable
    private fun SessionsListView(
        lifecycle: Lifecycle,
        sessionsList: List<SessionView>,
        onItemClickAction: (String) -> Unit,
        onItemActionName: String,
        askActionMessage: String,
        fetchCoachUsers: (List<String>) -> Unit,
        coachUsersFlow: SharedFlow<UsersListState>
    ) {
        var sessionData: SessionView by remember { mutableStateOf(SessionView(Session())) }

        var showActionDialog by remember { mutableStateOf(false) }
        if (showActionDialog) {
            DialogUtils.YesNoDialog(
                onOkPress = {
                    onItemClickAction(sessionData.session.id)
                    showActionDialog = false
                },
                onDismissRequest = { showActionDialog = false },
                questionText = askActionMessage
            )
        }

        var coaches: List<AppUser> by remember { mutableStateOf(ArrayList())}
        var showDataDialog by remember { mutableStateOf(false) }
        if (showDataDialog) {
            SessionDataScreen.SessionDataScreen(
                sessionData = sessionData,
                coachUsers = coaches,
                onDismissRequest = { showDataDialog = false },
                onItemAction = { sessionId ->
                    onItemClickAction(sessionId)
                },
                itemActionName = onItemActionName
            )
        }


        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                coachUsersFlow.collect { usersListState ->
                    when (usersListState) {
                        is UsersListState.Loaded -> {
                            coaches = usersListState.usersList
                            showDataDialog = true
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
                    .background(
                        color = colorResource(id = R.color.transparent_royal_blue),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .pointerInput(item) {
                        detectTapGestures(
                            onPress = { },
                            onDoubleTap = { },
                            onLongPress = {
                                sessionData = item
                                showActionDialog = true
                            },
                            onTap = {
                                sessionData = item
                                if (item.session.coachesIds == null) {
                                    fetchCoachUsers(ArrayList<String>())
                                } else {
                                    fetchCoachUsers(item.session.coachesIds!!)
                                }
                            }
                        )
                    }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 4.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = item.gymName,
                                color = Color.White,
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row {
                                Text(
                                    text = ResUtils.getString(R.string.caption_from),
                                    color = Color.White,
                                    style = MaterialTheme.typography.body1
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    color = Color.White,
                                    text = item.session.startTimeString,
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                color = Color.White,
                                text = item.sessionTypeName,
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row {
                                Text(
                                    text = ResUtils.getString(R.string.caption_until),
                                    color = Color.White,
                                    style = MaterialTheme.typography.body1
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = item.session.endTimeString,
                                    color = Color.White,
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}