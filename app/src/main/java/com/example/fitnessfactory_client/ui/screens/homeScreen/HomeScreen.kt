package com.example.fitnessfactory_client.ui.screens.homeScreen

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.Session
import com.example.fitnessfactory_client.data.views.SessionView
import com.example.fitnessfactory_client.ui.Components
import com.example.fitnessfactory_client.ui.components.SessionsListView
import com.example.fitnessfactory_client.ui.screens.mySessionsScreen.MySessionsListScreen
import com.example.fitnessfactory_client.ui.screens.mySessionsScreen.SessionViewsListState
import com.example.fitnessfactory_client.ui.uiState.ListState
import com.example.fitnessfactory_client.ui.uiState.ListStateOperator
import com.example.fitnessfactory_client.utils.GuiUtils
import com.example.fitnessfactory_client.utils.ResUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.collections.ArrayList

object HomeScreen {

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun HomeScreen(lifecycle: Lifecycle, openDrawer: () -> Unit) {
        val viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModelFactory())

        var date by rememberSaveable { mutableStateOf(Date()) }
        var showSessionsList by rememberSaveable { mutableStateOf(false) }
        val setListenerDate: (Date) -> Unit = {
            if (!showSessionsList) {
                showSessionsList = true
            }
            date = it
        }
        val subscribeToSession: (String) -> Unit = { sessionId ->
            GuiUtils.showMessage(sessionId)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Components.TopBar(
                title = ResUtils.getString(R.string.title_home_screen),
                buttonIcon = Icons.Filled.Menu,
                onButtonClicked = { openDrawer() })

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Components.HomeScreenCalendarView(
                    setListenerDate = setListenerDate
                )
            }

            if (showSessionsList) {
                SessionsListView.SessionsListViewScreen(
                    lifecycle = lifecycle,
                    date = date,
                    listStateFlow = viewModel.sessionViewsListState,
                    startDataListener = { listenerDate -> viewModel.startDataListener(date = listenerDate)},
                    onItemClickAction = subscribeToSession
                )
            }
        }
    }
}