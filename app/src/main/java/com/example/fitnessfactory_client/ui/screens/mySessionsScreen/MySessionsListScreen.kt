package com.example.fitnessfactory_client.ui.screens.mySessionsScreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.views.SessionView
import com.example.fitnessfactory_client.ui.Components
import com.example.fitnessfactory_client.utils.DialogUtils
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.TimeUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

object MySessionsListScreen {

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun MySessionsListScreen(lifecycle: Lifecycle, openDrawer: () -> Unit) {
        Column(modifier = Modifier.fillMaxSize()) {
            Components.TopBar(
                title = ResUtils.getString(R.string.title_sessions_screen),
                buttonIcon = Icons.Filled.Menu,
                onButtonClicked = { openDrawer() })
            var sessionsList: List<SessionView> by remember { mutableStateOf(ArrayList()) }

            val viewModel: MySessionsListScreenViewModel =
                viewModel(factory = MySessionsListScreenViewModelFactory())

            var listState: ListState by remember { mutableStateOf(ListState.Loading) }
            val listLoaded = {
                listState = ListState.Loaded
            }
            val listLoading = {
                listState = ListState.Loading
            }
            val listEmpty = {
                listState = ListState.Empty
            }

            LaunchedEffect(key1 = Unit) {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.sessionsListState.collect { sessionsListState ->
                        when (sessionsListState) {
                            is SessionsListState.Loaded -> {
                                sessionsList = sessionsListState.sessionsList
                                if (sessionsListState.sessionsList.isEmpty()) {
                                    listEmpty()
                                } else {
                                    listLoaded()
                                }
                            }
                            is SessionsListState.Error -> {
                                listLoaded()
                            }
                            is SessionsListState.Loading -> listLoading()
                        }
                    }
                }
            }

            var date by rememberSaveable { mutableStateOf(Date()) }
            val setDate: (Date) -> Unit = {
                date = it
            }
            LaunchedEffect(date) {
                viewModel.startDataListener(date = date)
            }

            if (listState !is ListState.Loading) {
                DatePickerView(date = date, setDate = setDate)
            }

            val unsubscribeSession: (String) -> Unit = { sessionId ->

                viewModel.unsubscribeFromSession(sessionId = sessionId)
            }
            when (listState) {
                is ListState.Loading -> ListLoadingView()
                is ListState.Empty -> ListEmptyView()
                is ListState.Loaded -> SessionsListView(
                    sessionsList = sessionsList,
                    unsubscribeSession = unsubscribeSession
                )
            }
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

    @Composable
    private fun DatePickerView(date: Date, setDate: (Date) -> Unit) {
        var showDatePicker by remember { mutableStateOf(false) }

        if (showDatePicker) {
            DialogUtils.DatePicker(
                onDateSelected = { selDate ->
                    setDate(selDate)
                    showDatePicker = false
                },
                onDismissRequest = { showDatePicker = false }
            )
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { showDatePicker = true },
            value = TimeUtils.dateToLocaleStr(date),
            onValueChange = {},
            enabled = false,
            label = { Text(ResUtils.getString(R.string.caption_date)) },
            textStyle = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.height(8.dp))
    }

    @ExperimentalMaterialApi
    @Composable
    private fun SessionsListView(
        sessionsList: List<SessionView>,
        unsubscribeSession: (String) -> Unit
    ) {
        var showDialog by remember { mutableStateOf(false) }
        var sessionId by remember { mutableStateOf("") }
        if (showDialog) {
            DialogUtils.YesNoDialog(
                onOkPress = {
                    unsubscribeSession(sessionId)
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
                .background(
                    color = colorResource(id = R.color.royalBlue),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            itemsIndexed(sessionsList) { index, item ->
                Column(modifier = Modifier
                    .fillMaxWidth()
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
                                    text = "from",
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
                                    text = "until",
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
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}