package com.example.fitnessfactory_client.utils

import androidx.annotation.FloatRange
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.fitnessfactory_client.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import androidx.annotation.*
import androidx.compose.ui.Alignment
import com.google.accompanist.pager.HorizontalPager

object DialogUtils {

    @ExperimentalPagerApi
    @Composable
    fun SingleSelectDialog(
        title: String,
        optionsList: List<String>,
        onSubmitButtonClick: (String) -> Unit,
        onDismissRequest: () -> Unit
    ) {


        Dialog(
            onDismissRequest = { onDismissRequest.invoke() },
            DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
        ) {
            Surface(
                modifier = Modifier.width(300.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(SizeUtils.dialogPadding)) {

                    Text(
                        text = title,
                        fontSize = SizeUtils.titleTextSize,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    var tabIndex by remember { mutableStateOf(0) }

                    val searchTabIndex = 0
                    val invitedTabIndex = 1
                    val tabData = listOf(
                        "Search",
                        "Invited",
                    )

                    val pagerState = rememberPagerState(
                        pageCount = tabData.size,
                        initialOffscreenLimit = 2,
                    )

                    TabRow(selectedTabIndex = tabIndex) {
                        tabData.forEachIndexed { index, text ->
                            Tab(selected = tabIndex == index, onClick = {
                                tabIndex = index
                            }, text = {
                                Text(text = text)
                            })
                        }
                    }

                    HorizontalPager(
                        state = pagerState
                    ) { index ->
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            when (index) {
                                searchTabIndex -> searchOwnerTab(
                                    optionsList = optionsList,
                                    onSubmitButtonClick = onSubmitButtonClick
                                )
                                invitedTabIndex -> Text("invited tab")
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
    private fun searchOwnerTab(optionsList: List<String>,
                               onSubmitButtonClick: (String) -> Unit) {
        var text by remember { mutableStateOf("")}
        var optionsListValues by remember { mutableStateOf(optionsList) }

        TextField(
            value = text,
            onValueChange = { value ->
                run {
                    text = value
                    optionsListValues =  optionsList.filter { it.contains(value) }
                }
            },
            label = { Text(ResUtils.getString(R.string.caption_search_by_name)) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.Center) {
            itemsIndexed(optionsListValues) { index, item ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(SizeUtils.dialogListItemHeight)
                    .clickable { onSubmitButtonClick(item) }) {
                    Text(
                        text = item,
                        textAlign = TextAlign.Start,
                        fontSize = SizeUtils.captionTextSize
                    )
                }
            }
        }
    }
}