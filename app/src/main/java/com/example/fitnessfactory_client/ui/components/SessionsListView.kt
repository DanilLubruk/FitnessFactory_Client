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
import com.example.fitnessfactory_client.data.views.SessionView
import com.example.fitnessfactory_client.ui.screens.mySessionsScreen.SessionViewsListState
import com.example.fitnessfactory_client.ui.uiState.ListState
import com.example.fitnessfactory_client.ui.uiState.ListStateOperator
import com.example.fitnessfactory_client.utils.DialogUtils
import com.example.fitnessfactory_client.utils.ResUtils
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
        onItemClickAction: (String) -> Unit
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
                sessionsList = sessionsList,
                onItemClickAction = onItemClickAction
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
        sessionsList: List<SessionView>,
        onItemClickAction: (String) -> Unit
    ) {
        var showDialog by remember { mutableStateOf(false) }
        var sessionId by remember { mutableStateOf("") }
        if (showDialog) {
            DialogUtils.YesNoDialog(
                onOkPress = {
                    onItemClickAction(sessionId)
                    showDialog = false
                },
                onDismissRequest = { showDialog = false },
                questionText = ResUtils.getString(R.string.message_unsubscribe_from_session)
            )
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
                        color = colorResource(id = R.color.royalBlue),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { },
                            onDoubleTap = { },
                            onLongPress = {
                                sessionId = item.session.id
                                showDialog = true
                            },
                            onTap = { }
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