package com.example.fitnessfactory_client.ui.screens.mySessionsScreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.views.SessionView
import com.example.fitnessfactory_client.ui.Components
import com.example.fitnessfactory_client.ui.components.SessionsListView
import com.example.fitnessfactory_client.ui.uiState.ListState
import com.example.fitnessfactory_client.ui.uiState.ListStateOperator
import com.example.fitnessfactory_client.utils.DialogUtils
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils
import com.example.fitnessfactory_client.utils.TimeUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.collections.ArrayList

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
            val viewModel: MySessionsListScreenViewModel =
                viewModel(factory = MySessionsListScreenViewModelFactory())

            var date by rememberSaveable { mutableStateOf(Date()) }
            val setDate: (Date) -> Unit = {
                date = it
            }

            DatePickerView(date = date, setDate = setDate)

            val unsubscribeSession: (String) -> Unit = { sessionId ->
                viewModel.unsubscribeFromSession(sessionId = sessionId)
            }

            SessionsListView.SessionsListViewScreen(
                lifecycle = lifecycle,
                date = date,
                listStateFlow = viewModel.sessionViewsListState,
                startDataListener = { listenerDate -> viewModel.startDataListener(listenerDate)},
                onItemClickAction = unsubscribeSession,
                StringUtils.getMessageUnsubscribeFromSession()
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
}