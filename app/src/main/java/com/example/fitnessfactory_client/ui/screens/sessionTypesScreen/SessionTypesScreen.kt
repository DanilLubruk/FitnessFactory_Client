package com.example.fitnessfactory_client.ui.screens.sessionTypesScreen

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
import com.example.fitnessfactory_client.data.models.SessionType
import com.example.fitnessfactory_client.ui.components.ListEmptyView
import com.example.fitnessfactory_client.ui.components.ListLoadingView
import com.example.fitnessfactory_client.ui.components.TopBar
import com.example.fitnessfactory_client.utils.ResUtils
import com.example.fitnessfactory_client.utils.StringUtils

object SessionTypesScreen {

    @Composable
    fun SessionTypesScreen(
        lifecycle: Lifecycle,
        openDrawer: () -> Unit,
        showSessionsAction: (SessionType) -> Unit
    ) {
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

        Column(modifier = Modifier.fillMaxSize()) {

            TopBar.TopBar(
                title = ResUtils.getString(R.string.title_coaches_screen),
                buttonIcon = Icons.Filled.Menu,
                onButtonClicked = { openDrawer() },
            )

            when (typesListState) {
                is SessionTypesListState.Loaded -> {
                    val typesList = (typesListState as SessionTypesListState.Loaded).sessionTypes
                    if (typesList.isNotEmpty()) {
                        SessionTypesListView(
                            typesList = typesList,
                            showSessionsAction = showSessionsAction
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

    @Composable
    private fun SessionTypesListView(
        typesList: ArrayList<SessionType>,
        showSessionsAction: (SessionType) -> Unit
    ) {
        var sessionType by remember { mutableStateOf(SessionType()) }
        var showDataDialog by remember { mutableStateOf(false) }
        if (showDataDialog) {
            SessionTypeDataScreen.SessionTypeDataScreen(
                sessionType = sessionType,
                onDismissRequest = { showDataDialog = false },
                showSessionsAction = showSessionsAction)
        }

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            itemsIndexed(typesList) { index, item ->
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
                                sessionType = item
                                showDataDialog = true
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
                                text = item.name,
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