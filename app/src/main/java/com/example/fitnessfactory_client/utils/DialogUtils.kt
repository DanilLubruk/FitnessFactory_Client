package com.example.fitnessfactory_client.utils

import android.widget.CalendarView
import androidx.annotation.FloatRange
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.fitnessfactory_client.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fitnessfactory_client.data.beans.OwnersData
import com.example.fitnessfactory_client.data.models.Owner
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.launch
import java.util.*

object DialogUtils {

    @ExperimentalPagerApi
    @Composable
    fun SingleGymOwnerSelectDialog(
        title: String,
        ownersData: OwnersData,
        onSubmitButtonClick: (Owner) -> Unit,
        onDismissRequest: () -> Unit
    ) {
        val allOwnersList = ownersData.allOwnersList
        val invitedOwnersList = ownersData.invitedOwnersList

        Dialog(
            onDismissRequest = { onDismissRequest.invoke() }
        ) {
            Surface(
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column {
                    val invitedTabIndex = 0
                    val searchTabIndex = 1
                    val tabData = listOf(
                        stringResource(id = R.string.caption_invited),
                        stringResource(id = R.string.caption_search),
                    )

                    val pagerState = rememberPagerState(
                        pageCount = tabData.size,
                        initialOffscreenLimit = 2,
                    )

                    val tabIndex = pagerState.currentPage
                    val coroutineScope = rememberCoroutineScope()

                    TabRow(
                        selectedTabIndex = tabIndex,
                        backgroundColor = colorResource(id = R.color.royalBlue),
                        contentColor = Color.White,
                        modifier = Modifier.height(48.dp),
                    ) {
                        tabData.forEachIndexed { index, text ->
                            Tab(
                                selected = tabIndex == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = text,
                                            style = MaterialTheme.typography.body1
                                        )
                                    }
                                }
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.absolutePadding(
                            left = SizeUtils.dialogPadding,
                            right = SizeUtils.dialogPadding,
                            bottom = SizeUtils.dialogPadding
                        )
                    ) {
                        HorizontalPager(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            state = pagerState
                        ) { index ->
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                when (index) {
                                    searchTabIndex -> SearchOwnerTab(
                                        optionsList = allOwnersList,
                                        onSubmitButtonClick = onSubmitButtonClick
                                    )
                                    invitedTabIndex -> SearchOwnerTab(
                                        optionsList = invitedOwnersList,
                                        onSubmitButtonClick = onSubmitButtonClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    fun rememberPagerState(
        @androidx.annotation.IntRange(from = 0) pageCount: Int,
        @androidx.annotation.IntRange(from = 0) initialPage: Int = 0,
        @FloatRange(from = 0.0, to = 1.0) initialPageOffset: Float = 0f,
        @androidx.annotation.IntRange(from = 1) initialOffscreenLimit: Int = 1,
        infiniteLoop: Boolean = true
    ): PagerState = rememberSaveable(saver = PagerState.Saver) {
        PagerState(
            pageCount = pageCount,
            currentPage = initialPage,
            currentPageOffset = initialPageOffset,
            offscreenLimit = initialOffscreenLimit,
            infiniteLoop = infiniteLoop
        )
    }

    @Composable
    private fun SearchOwnerTab(
        optionsList: List<Owner>,
        onSubmitButtonClick: (Owner) -> Unit
    ) {
        var text by remember { mutableStateOf("") }
        var optionsListValues by remember { mutableStateOf(optionsList) }

        OutlinedTextField(
            singleLine = true,
            value = text,
            onValueChange = { value ->
                run {
                    text = value
                    optionsListValues =
                        optionsList.filter {
                            it.organisationName.lowercase(Locale.getDefault())
                                .contains(value.lowercase(Locale.getDefault()))
                        }
                }
            },
            label = { Text(ResUtils.getString(R.string.caption_search_by_name)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            itemsIndexed(optionsListValues) { index, owner ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(SizeUtils.dialogListItemHeight)
                    .padding(8.dp)
                    .clickable { onSubmitButtonClick(owner) }) {
                    Text(
                        text = owner.organisationName,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }

    @Composable
    fun DatePicker(onDateSelected: (Date) -> Unit, onDismissRequest: () -> Unit) {
        var selDate by remember { mutableStateOf(Date()) }

        Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = colorResource(id = R.color.royalBlue),
                        shape = RoundedCornerShape(size = 16.dp)
                    )
            ) {
                Column(
                    Modifier
                        .defaultMinSize(minHeight = 72.dp)
                        .fillMaxWidth()
                        .background(
                            color = colorResource(id = R.color.royalBlue),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = ResUtils.getString(R.string.caption_select_date)
                            .uppercase(Locale.ENGLISH),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onPrimary
                    )

                    Spacer(modifier = Modifier.size(24.dp))

                    Text(
                        text = TimeUtils.dateToLocaleStr(selDate),
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onPrimary
                    )

                    Spacer(modifier = Modifier.size(16.dp))
                }

                CustomCalendarView(onDateSelected = {
                    selDate = it
                })

                Spacer(modifier = Modifier.size(8.dp))

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 16.dp, end = 16.dp)
                ) {
                    TextButton(
                        onClick = onDismissRequest
                    ) {
                        Text(
                            text = ResUtils.getString(R.string.caption_cancel),
                            style = MaterialTheme.typography.button
                        )
                    }

                    TextButton(
                        onClick = {
                            onDateSelected(selDate)
                            onDismissRequest()
                        }
                    ) {
                        Text(
                            text = ResUtils.getString(R.string.caption_ok),
                            style = MaterialTheme.typography.button
                        )
                    }

                }
            }
        }
    }

    @Composable
    fun CustomCalendarView(onDateSelected: (Date) -> Unit) {
        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { context ->
                CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom))
            },
            update = { view ->
                view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    onDateSelected(calendar.time)
                }
            }
        )
    }

    @Composable
    fun YesNoDialog(
        onOkPress: () -> Unit,
        onDismissRequest: () -> Unit,
        questionText: String
    ) {
        Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(size = 16.dp)
                    )
            ) {
                Column(
                    Modifier
                        .defaultMinSize(minHeight = 72.dp)
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = questionText,
                        style = MaterialTheme.typography.body1
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 16.dp, end = 16.dp)
                ) {
                    TextButton(
                        onClick = onDismissRequest
                    ) {
                        Text(
                            text = ResUtils.getString(R.string.caption_cancel),
                            style = MaterialTheme.typography.button
                        )
                    }

                    TextButton(
                        onClick = {
                            onOkPress()
                            onDismissRequest()
                        }
                    ) {
                        Text(
                            text = ResUtils.getString(R.string.caption_ok),
                            style = MaterialTheme.typography.button
                        )
                    }

                }
            }
        }
    }
}