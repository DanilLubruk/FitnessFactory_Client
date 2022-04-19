package com.example.fitnessfactory_client.ui.screens.mySessionsScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.data.views.SessionView
import com.example.fitnessfactory_client.ui.components.DataScreenField
import com.example.fitnessfactory_client.ui.components.SessionDataScreen
import com.example.fitnessfactory_client.ui.components.SessionsListView
import com.example.fitnessfactory_client.ui.components.TopBar
import com.example.fitnessfactory_client.ui.drawer.DrawerScreens
import com.example.fitnessfactory_client.ui.screens.sessionTypesScreen.SessionTypeDataScreen
import com.example.fitnessfactory_client.utils.*
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*

object MySessionsListScreen {

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun MySessionsListScreen(lifecycle: Lifecycle, openDrawer: () -> Unit) {
        val modalBottomSheetState =
            rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                skipHalfExpanded = false
            )
        val scope = rememberCoroutineScope()

        val viewModel: MySessionsListScreenViewModel =
            viewModel(factory = MySessionsListScreenViewModelFactory())

        val unsubscribeSession: (String) -> Unit = { sessionId ->
            viewModel.unsubscribeFromSession(sessionId = sessionId)
        }
        var sessionData: SessionView by remember { mutableStateOf(SessionView(Session())) }
        var coaches: List<AppUser> by remember { mutableStateOf(ArrayList()) }

        val showBottomSheet: (SessionView, List<AppUser>) -> Unit = { session, sessionCoaches ->
            scope.launch {
                sessionData = session
                coaches = sessionCoaches
                modalBottomSheetState.show()
            }
        }

        ModalBottomSheetLayout(
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = {
                SessionDataScreen.SessionDataScreen(
                    sessionData = sessionData,
                    coachUsers = coaches,
                    onItemAction = { sessionId ->
                        unsubscribeSession(sessionId)
                    },
                    itemActionName = ResUtils.getString(R.string.caption_unsubscribe),
                    modalBottomSheetState = modalBottomSheetState
                )
            },
            sheetState = modalBottomSheetState,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopBar.TopBar(
                    title = DrawerScreens.MySessions.title,
                    buttonIcon = Icons.Filled.Menu,
                    onButtonClicked = { openDrawer() })

                var startDate by rememberSaveable { mutableStateOf(TimeUtils.getStartOfDayDate(Date())) }
                var endDate by rememberSaveable { mutableStateOf(TimeUtils.getStartOfDayDate(Date())) }
                val setRange: (Date, Date) -> Unit = { selectedStartDate, selectedEndDate ->
                    startDate = selectedStartDate
                    endDate = selectedEndDate
                }

                DatePickerView(startDate = startDate, endDate = endDate, setRange = setRange)

                SessionsListView.SessionsListViewScreen(
                    lifecycle = lifecycle,
                    startDate = startDate,
                    endDate = endDate,
                    listStateFlow = viewModel.sessionViewsListState,
                    startDataListener = { selectedStartDate, selectedEndDate ->
                        viewModel.startDataListener(
                            selectedStartDate, selectedEndDate
                        )
                    },
                    fetchCoachUsers = { coachesEmails -> viewModel.fetchCoachUsers(coachesEmails = coachesEmails) },
                    coachUsersFlow = viewModel.coachesListState,
                    showBottomSheet = showBottomSheet,
                )
            }
        }
    }

    @Composable
    private fun DatePickerView(startDate: Date, endDate: Date, setRange: (Date, Date) -> Unit) {
        var showDatePicker by remember { mutableStateOf(false) }

        if (showDatePicker) {
            DialogUtils.RangePicker(
                startDate = startDate,
                endDate = endDate,
                onRangeSelected = { selectedStartDate, selectedEndDate ->
                    setRange(
                        TimeUtils.getStartOfDayDate(selectedStartDate),
                        TimeUtils.getStartOfDayDate(selectedEndDate)
                    )
                    showDatePicker = false
                },
                onDismissRequest = { showDatePicker = false }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                value = if (startDate.equals(endDate))
                    TimeUtils.dateToLocaleStr(startDate)
                else "${
                    TimeUtils.dateToLocaleStr(
                        startDate
                    )
                } - ${TimeUtils.dateToLocaleStr(endDate)}",
                onValueChange = {},
                label = { Text(ResUtils.getString(R.string.caption_date)) },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable { showDatePicker = true }
            )
        }
    }
}