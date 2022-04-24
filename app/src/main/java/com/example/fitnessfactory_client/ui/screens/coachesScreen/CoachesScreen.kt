package com.example.fitnessfactory_client.ui.screens.coachesScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.CoachData
import com.example.fitnessfactory_client.data.beans.TopBarAction
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.ui.components.*
import com.example.fitnessfactory_client.ui.drawer.DrawerScreens
import com.example.fitnessfactory_client.utils.StringUtils
import kotlinx.coroutines.launch

object CoachesScreen {

    @OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
    @Composable
    fun CoachesScreen(
        lifecycle: Lifecycle,
        openDrawer: () -> Unit,
        showSessionsAction: (CoachData) -> Unit,
        navigateHome: () -> Unit,
    ) {
        BackHandler {
            navigateHome()
        }

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
            rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                skipHalfExpanded = false
            )
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

        var showSearch by rememberSaveable { mutableStateOf(false) }
        var searchText by rememberSaveable { mutableStateOf("") }
        var searchFieldState: CoachSearchFieldState by remember { mutableStateOf(CoachSearchFieldState.CoachNameFieldSearch) }

        ModalBottomSheetLayout(
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = {
                CoachDataScreen.CoachDataScreen(
                    coach = coach,
                    gymsList = gymsList,
                    showSessionsAction = { coachUserId ->
                        viewModel.fetchCoachForFilter(
                            coachUserId
                        )
                    })
            },
            sheetState = modalBottomSheetState,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                val keyboardController = LocalSoftwareKeyboardController.current

                TopBar.TopBar(
                    title = DrawerScreens.Coaches.title,
                    buttonIcon = Icons.Filled.Menu,
                    onButtonClicked = { openDrawer() },
                    actions = listOf(
                        TopBarAction(
                            stringResource(id = R.string.caption_search),
                            image = Icons.Filled.Search,
                            imageTint = if (showSearch) Color.Yellow else Color.White
                        ) {
                            searchText = ""
                            showSearch = !showSearch
                            if (!showSearch) {
                                keyboardController?.hide()
                            }
                        }
                    )
                )

                AnimatedVisibility(visible = showSearch) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(15f)
                                .padding(start = 16.dp),
                            value = searchText,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {keyboardController?.hide()}
                            ),
                            onValueChange = {
                                searchText = it
                            },
                            label = { Text(searchFieldState.toString()) },
                            textStyle = MaterialTheme.typography.body1,
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.Black,
                                backgroundColor = Color.White
                            )
                        )

                        var toogle by remember { mutableStateOf(false) }

                        IconButton(
                            modifier = Modifier.weight(1f),
                            onClick = { toogle = !toogle }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = ""
                            )
                            DropDownList.DropDownList(
                                isToogled = toogle,
                                items = listOf(
                                    CoachSearchFieldState.CoachNameFieldSearch,
                                    CoachSearchFieldState.CoachEmailFieldSearch
                                ),
                                selectItem = { item ->
                                    searchFieldState = item
                                },
                                setToogle = { toogled -> toogle = toogled }
                            )
                        }
                    }
                }

                when (coachesListState) {
                    is CoachesListState.Loaded -> {
                        var coachesList = (coachesListState as CoachesListState.Loaded).coachesList
                        if (coachesList.isNotEmpty()) {
                            coachesList = coachesList.filter { appUser ->
                                searchFieldState.getSearchField(appUser)
                                    .contains(searchText.toLowerCase(Locale.current))
                            }
                            CoachesList(
                                coachesList = coachesList,
                                fetchGyms = { selectedCoach ->
                                    coach = selectedCoach
                                    viewModel.fetchGymsList(selectedCoach.id)
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