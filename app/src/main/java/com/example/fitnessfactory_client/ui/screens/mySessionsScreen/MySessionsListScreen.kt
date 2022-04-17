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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import com.example.fitnessfactory_client.utils.DialogUtils
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils
import com.example.fitnessfactory_client.utils.TimeUtils
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

                var date by rememberSaveable { mutableStateOf(Date()) }
                val setDate: (Date) -> Unit = {
                    date = it
                }

                DatePickerView(date = date, setDate = setDate)

                SessionsListView.SessionsListViewScreen(
                    lifecycle = lifecycle,
                    date = date,
                    listStateFlow = viewModel.sessionViewsListState,
                    startDataListener = { listenerDate -> viewModel.startDataListener(listenerDate) },
                    fetchCoachUsers = { coachesEmails -> viewModel.fetchCoachUsers(coachesEmails = coachesEmails) },
                    coachUsersFlow = viewModel.coachesListState,
                    showBottomSheet = showBottomSheet,
                )
            }
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                value = TimeUtils.dateToLocaleStr(date),
                onValueChange = {},
                label = { Text(ResUtils.getString(R.string.caption_date)) },
                textStyle = MaterialTheme.typography.body1
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