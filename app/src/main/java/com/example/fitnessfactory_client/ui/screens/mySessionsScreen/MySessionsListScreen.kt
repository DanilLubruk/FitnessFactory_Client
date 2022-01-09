package com.example.fitnessfactory_client.ui.screens.mySessionsScreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.views.SessionView
import com.example.fitnessfactory_client.ui.Components
import com.example.fitnessfactory_client.utils.ResUtils
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
            var sessionsList: List<SessionView> by remember { mutableStateOf(ArrayList()) }
            var isLoading: Boolean by remember { mutableStateOf(false) }
            val viewModel: MySessionsListScreenViewModel = viewModel(factory = MySessionsListScreenViewModelFactory())

            LaunchedEffect(key1 = Unit) {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.sessionsListState.collect { sessionsListState ->
                        when (sessionsListState) {
                            is SessionsListState.Loaded -> {
                                sessionsList = sessionsListState.sessionsList
                                isLoading = false
                            }
                            is SessionsListState.Error -> {
                                isLoading = false
                            }
                            is SessionsListState.Loading -> isLoading = true
                        }
                    }
                }
            }

            LaunchedEffect(key1 = Unit) {
                viewModel.startDataListener(Date())
            }

            if (isLoading) {
                ListLoadingView()
            } else {
                SessionsListView(sessionsList = sessionsList)
            }
        }
    }

    @Composable
    private fun ListLoadingView() {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colors.primary
            )
        }
    }

    @Composable
    private fun SessionsListView(sessionsList: List<SessionView>) {
        LazyColumn {
            itemsIndexed(sessionsList) { index, item ->
                Text(text = item.session.dateString)
                Text(text = item.gymName)
                Text(text = item.sessionTypeName)
            }
        }
    }
}