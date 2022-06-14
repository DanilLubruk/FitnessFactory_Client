package com.example.fitnessfactory_client.ui.screens.sessionTypesScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.ripple.rememberRipple
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
import com.example.fitnessfactory_client.data.models.SessionType
import com.example.fitnessfactory_client.ui.components.*
import com.example.fitnessfactory_client.ui.drawer.DrawerScreens
import com.example.fitnessfactory_client.ui.screens.gymsScreen.GymSearchFieldState
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SessionTypesScreen {

    @OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
    @Composable
    fun SessionTypesScreen(
        lifecycle: Lifecycle,
        openDrawer: () -> Unit,
        showSessionsAction: (SessionType) -> Unit,
        navigateHome: () -> Unit,
    ) {
        BackHandler {
            navigateHome()
        }
        val viewModel: SessionTypesScreenViewModel =
            viewModel(factory = SessionTypesScreenViewModelFactory())
        var typesListState: SessionTypesListState by remember { mutableStateOf(SessionTypesListState.Loading) }

        LaunchedEffect(key1 = Unit) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.typesListState.collect { listState ->
                    typesListState = listState
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

        var sessionType by remember { mutableStateOf(SessionType()) }
        val showBottomSheet: (SessionType) -> Unit = {
            scope.launch {
                sessionType = it
                modalBottomSheetState.show()
            }
        }

        ModalBottomSheetLayout(
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = {
                SessionTypeDataScreen.SessionTypeDataScreen(
                    sessionType = sessionType,
                    showSessionsAction = showSessionsAction,
                )
            },
            sheetState = modalBottomSheetState,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                val keyboardController = LocalSoftwareKeyboardController.current
                var showSearch by rememberSaveable { mutableStateOf(false) }
                var searchText by rememberSaveable { mutableStateOf("") }

                TopBar.TopBar(
                    title = DrawerScreens.SessionTypes.title,
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
                                .padding(start = 16.dp, end = 16.dp),
                            value = searchText,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {keyboardController?.hide()}
                            ),
                            onValueChange = {
                                searchText = it
                            },
                            label = { Text(stringResource(id = R.string.caption_name)) },
                            textStyle = MaterialTheme.typography.body1,
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.Black,
                                backgroundColor = Color.White
                            )
                        )
                    }
                }

                when (typesListState) {
                    is SessionTypesListState.Loaded -> {
                        var typesList =
                            (typesListState as SessionTypesListState.Loaded).sessionTypes
                        if (typesList.isNotEmpty()) {
                            typesList = ArrayList(typesList.filter { type ->
                                type.name.toLowerCase(Locale.current)
                                    .contains(searchText.toLowerCase(Locale.current))
                            })
                            SessionTypesListView(
                                typesList = typesList,
                                showBottomSheet = showBottomSheet
                            )
                        } else {
                            ListEmptyView.ListEmptyView(
                                emptyListCaption = StringUtils.getCaptionEmptySessionTypesList()
                            )
                        }
                    }
                    is SessionTypesListState.Loading -> ListLoadingView.ListLoadingView()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun SessionTypesListView(
        typesList: ArrayList<SessionType>,
        showBottomSheet: (SessionType) -> Unit,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            itemsIndexed(typesList) { index, item ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .pointerInput(item) {
                        detectTapGestures(
                            onPress = { },
                            onDoubleTap = { },
                            onLongPress = {},
                            onTap = {
                                showBottomSheet(item)
                            }
                        )
                    }) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.name,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                        Text(
                            color = Color.Gray,
                            text = StringUtils.getPriceTag(item.price),
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            color = Color.Gray,
                            text = StringUtils.getPeopleCaption(item.peopleAmount),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}