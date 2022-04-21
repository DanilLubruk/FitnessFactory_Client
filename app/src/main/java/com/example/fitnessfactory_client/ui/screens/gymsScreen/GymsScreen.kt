package com.example.fitnessfactory_client.ui.screens.gymsScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.beans.TopBarAction
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.SessionType
import com.example.fitnessfactory_client.ui.components.*
import com.example.fitnessfactory_client.ui.drawer.DrawerScreens
import com.example.fitnessfactory_client.ui.screens.coachesScreen.CoachSearchFieldState
import com.example.fitnessfactory_client.ui.screens.sessionTypesScreen.SessionTypeDataScreen
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

object GymsScreen {

    @OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
    @Composable
    fun GymsScreen(
        lifecycle: Lifecycle,
        openDrawer: () -> Unit,
        showSessionsAction: (Gym) -> Unit,
        navigateHome: () -> Unit,
    ) {
        BackHandler {
            navigateHome()
        }
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
        val modalBottomSheetState =
            rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                skipHalfExpanded = false
            )
        val scope = rememberCoroutineScope()
        var gym by remember { mutableStateOf(Gym()) }
        var coachesList by remember { mutableStateOf(ArrayList<AppUser>()) }
        val showBottomSheet: (Gym, ArrayList<AppUser>) -> Unit = { selectedGym, coaches ->
            scope.launch {
                gym = selectedGym
                coachesList = coaches
                modalBottomSheetState.show()
            }
        }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coachesDataList.collect {
                    coachesList = it
                    showBottomSheet(gym, coachesList)
                }
            }
        }

        ModalBottomSheetLayout(
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = {
                GymDataScreen.GymDataScreen(
                    gym = gym,
                    coaches = coachesList,
                    showSessionsAction = showSessionsAction
                )
            },
            sheetState = modalBottomSheetState,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                var showSearch by rememberSaveable { mutableStateOf(false) }
                var searchText by rememberSaveable { mutableStateOf("") }
                var searchFieldState: GymSearchFieldState by remember {
                    mutableStateOf(
                        GymSearchFieldState.GymNameFieldSearch
                    )
                }

                TopBar.TopBar(
                    title = DrawerScreens.Gyms.title,
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
                        }
                    )
                )
                val keyboardController = LocalSoftwareKeyboardController.current

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
                            onValueChange = {
                                searchText = it
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {keyboardController?.hide()}
                            ),
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
                                    GymSearchFieldState.GymNameFieldSearch,
                                    GymSearchFieldState.GymAddressFieldSearch
                                ),
                                selectItem = { item ->
                                    searchFieldState = item
                                },
                                setToogle = { toogled -> toogle = toogled }
                            )
                        }
                    }
                }

                when (gymsListState) {
                    is GymsListState.Loaded -> {
                        var gymsList = (gymsListState as GymsListState.Loaded).gymsList
                        if (gymsList.isNotEmpty()) {
                            gymsList = ArrayList(gymsList.filter { gym ->
                                searchFieldState.getSearchField(gym)
                                    .contains(searchText.toLowerCase(Locale.current))
                            })
                            GymsList(
                                gymsList = gymsList,
                                fetchCoaches = { selectedGym ->
                                    gym = selectedGym
                                    viewModel.fetchCoachesData(gymId = gym.id)
                                },
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
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun GymsList(
        gymsList: ArrayList<Gym>,
        fetchCoaches: (Gym) -> Unit,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            itemsIndexed(gymsList) { index, item ->
                GymListItemView.GymListItemView(
                    item = item,
                    onTap = { gym -> fetchCoaches(gym) }
                )
            }
        }
    }
}