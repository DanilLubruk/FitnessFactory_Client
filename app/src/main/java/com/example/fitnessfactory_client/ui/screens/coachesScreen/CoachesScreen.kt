package com.example.fitnessfactory_client.ui.screens.coachesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.CoachData
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.ui.components.ListEmptyView
import com.example.fitnessfactory_client.ui.components.ListLoadingView
import com.example.fitnessfactory_client.ui.components.TopBar
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils
import kotlinx.coroutines.flow.SharedFlow

object CoachesScreen {

    @Composable
    fun CoachesScreen(
        lifecycle: Lifecycle,
        openDrawer: () -> Unit,
        showSessionsAction: (CoachData) -> Unit
    ) {
        val viewModel: CoachesScreenViewModel = viewModel(factory = CoachesScreenViewModelFactory())
        var coachesListState: CoachesListState by remember { mutableStateOf(CoachesListState.Loading) }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coachesListState.collect { listState ->
                    coachesListState = listState
                }
            }
        }

        LaunchedEffect(key1 = Unit) {
            viewModel.startDataListener()
        }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coachForFilter.collect { coachData ->
                    showSessionsAction(coachData)
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {

            TopBar.TopBar(
                title = ResUtils.getString(R.string.title_coaches_screen),
                buttonIcon = Icons.Filled.Menu,
                onButtonClicked = { openDrawer() },
            )

            when (coachesListState) {
                is CoachesListState.Loaded -> {
                    val coachesList = (coachesListState as CoachesListState.Loaded).coachesList
                    if (coachesList.isNotEmpty()) {
                        CoachesList(
                            lifecycle = lifecycle,
                            coachesList = coachesList,
                            gymsFlow = viewModel.gymsList,
                            fetchGyms = { viewModel.fetchGymsList() },
                            fetchCoachData = { coachEmail -> viewModel.fetchCoachForFilter(coachEmail) }
                        )
                    } else {
                        ListEmptyView.ListEmptyView(
                            emptyListCaption = StringUtils.getCaptionEmptyCoachesList()
                        )
                    }
                }
                is CoachesListState.Loading -> ListLoadingView.ListLoadingView()
            }
        }
    }

    @Composable
    private fun CoachesList(
        lifecycle: Lifecycle,
        coachesList: List<AppUser>,
        gymsFlow: SharedFlow<ArrayList<Gym>>,
        fetchGyms: () -> Unit,
        fetchCoachData: (String) -> Unit
    ) {
        var coach by remember { mutableStateOf(AppUser()) }
        var gymsList by remember { mutableStateOf(ArrayList<Gym>()) }

        var showDataDialog by remember { mutableStateOf(false) }
        if (showDataDialog) {
            CoachDataScreen.CoachDataScreen(
                coach = coach,
                gymsList = gymsList,
                onDismissRequest = { showDataDialog = false },
                showSessionsAction = { coachEmail -> fetchCoachData(coachEmail) })
        }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                gymsFlow.collect {
                    gymsList = it
                    showDataDialog = true
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            itemsIndexed(coachesList) { index, item ->
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
                            onLongPress = {},
                            onTap = {
                                coach = item
                                fetchGyms()
                            }
                        )
                    }) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = item.name,
                                    color = Color.White,
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    color = Color.White,
                                    text = item.email,
                                    style = MaterialTheme.typography.body1
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