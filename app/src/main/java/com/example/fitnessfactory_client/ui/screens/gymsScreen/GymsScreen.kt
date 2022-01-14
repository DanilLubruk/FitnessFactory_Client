package com.example.fitnessfactory_client.ui.screens.gymsScreen

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
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.ui.components.ListEmptyView
import com.example.fitnessfactory_client.ui.components.ListLoadingView
import com.example.fitnessfactory_client.ui.components.TopBar
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils
import kotlinx.coroutines.flow.SharedFlow

object GymsScreen {

    @Composable
    fun GymsScreen(
        lifecycle: Lifecycle,
        openDrawer: () -> Unit,
        showSessionsAction: (Gym) -> Unit
    ) {
        val viewModel: GymsScreenViewModel = viewModel(factory = GymsScreenViewModelFactory())
        var gymsListState: GymsListState by remember { mutableStateOf(GymsListState.Loading) }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gymsListState.collect { listState ->
                    gymsListState = listState
                }
            }
        }

        LaunchedEffect(key1 = Unit) {
            viewModel.startDataListener()
        }

        Column(modifier = Modifier.fillMaxSize()) {

            TopBar.TopBar(
                title = ResUtils.getString(R.string.title_coaches_screen),
                buttonIcon = Icons.Filled.Menu,
                onButtonClicked = { openDrawer() },
            )

            when (gymsListState) {
                is GymsListState.Loaded -> {
                    val gymsList = (gymsListState as GymsListState.Loaded).gymsList
                    if (gymsList.isNotEmpty()) {
                        GymsList(
                            lifecycle = lifecycle,
                            gymsList = gymsList,
                            coachesFlow = viewModel.coachesDataList,
                            fetchCoaches = { gymId -> viewModel.fetchCoachesData(gymId = gymId) },
                            showSessionsAction = showSessionsAction
                        )
                    } else {
                        ListEmptyView.ListEmptyView(
                            emptyListCaption = StringUtils.getCaptionEmptyCoachesList()
                        )
                    }
                }
                is GymsListState.Loading -> ListLoadingView.ListLoadingView()
            }
        }
    }

    @Composable
    private fun GymsList(
        lifecycle: Lifecycle,
        gymsList: ArrayList<Gym>,
        coachesFlow: SharedFlow<ArrayList<AppUser>>,
        fetchCoaches: (String) -> Unit,
        showSessionsAction: (Gym) -> Unit
    ) {
        var gym by remember { mutableStateOf(Gym()) }
        var coachesList by remember { mutableStateOf(ArrayList<AppUser>()) }

        var showDataDialog by remember { mutableStateOf(false) }
        if (showDataDialog) {
            GymDataScreen.GymDataScreen(
                gym = gym,
                coaches = coachesList,
                onDismissRequest = { showDataDialog = false },
                showSessionsAction = showSessionsAction
            )
        }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                coachesFlow.collect {
                    coachesList = it
                    showDataDialog = true
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            itemsIndexed(gymsList) { index, item ->
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
                                gym = item
                                fetchCoaches(gym.id)
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
                            .padding(4.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                color = Color.White,
                                text = item.address,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}