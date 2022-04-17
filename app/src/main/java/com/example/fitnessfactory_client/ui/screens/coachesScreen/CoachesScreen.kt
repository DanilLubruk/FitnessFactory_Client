package com.example.fitnessfactory_client.ui.screens.coachesScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.CoachData
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.ui.components.CoachListItemView
import com.example.fitnessfactory_client.ui.components.ListEmptyView
import com.example.fitnessfactory_client.ui.components.ListLoadingView
import com.example.fitnessfactory_client.ui.components.TopBar
import com.example.fitnessfactory_client.ui.drawer.DrawerScreens
import com.example.fitnessfactory_client.ui.screens.gymsScreen.GymDataScreen
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

object CoachesScreen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun CoachesScreen(
        lifecycle: Lifecycle,
        openDrawer: () -> Unit,
        showSessionsAction: (CoachData) -> Unit,
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
        val modalBottomSheetState =
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = false)
        val scope = rememberCoroutineScope()
        var coach by remember { mutableStateOf(AppUser()) }
        var gymsList by remember { mutableStateOf(ArrayList<Gym>()) }

        val showBottomSheet: (AppUser, ArrayList<Gym>) -> Unit = { selectedCoach, gyms ->
            scope.launch {
                coach = selectedCoach
                gymsList = gyms
                modalBottomSheetState.show()
            }
        }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gymsList.collect {
                    showBottomSheet(coach, it)
                }
            }
        }

        ModalBottomSheetLayout(
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = {
                CoachDataScreen.CoachDataScreen(
                    coach = coach,
                    gymsList = gymsList,
                    showSessionsAction = { coachEmail ->
                        viewModel.fetchCoachForFilter(
                            coachEmail
                        )
                    })
            },
            sheetState = modalBottomSheetState,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                TopBar.TopBar(
                    title = DrawerScreens.Coaches.title,
                    buttonIcon = Icons.Filled.Menu,
                    onButtonClicked = { openDrawer() },
                )

                when (coachesListState) {
                    is CoachesListState.Loaded -> {
                        val coachesList = (coachesListState as CoachesListState.Loaded).coachesList
                        if (coachesList.isNotEmpty()) {
                            CoachesList(
                                coachesList = coachesList,
                                fetchGyms = { selectedCoach ->
                                    coach = selectedCoach
                                    viewModel.fetchGymsList(selectedCoach.email)
                                },
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
    }

    @Composable
    private fun CoachesList(
        coachesList: List<AppUser>,
        fetchGyms: (AppUser) -> Unit,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            itemsIndexed(coachesList) { index, item ->
                CoachListItemView.CoachListItemView(
                    item = item,
                    onTap = { fetchGyms(item) }
                )
            }
        }
    }
}