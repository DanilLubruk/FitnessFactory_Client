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
import androidx.compose.ui.Alignment
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
            onDismissRequest = { onDismissRequest.invoke() },
            DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
        ) {
            Surface(
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(SizeUtils.dialogPadding)) {

                    Text(
                        text = title,
                        style = MaterialTheme.typography.h4
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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

                    val tabIndex = pagerState.currentPage
                    val coroutineScope = rememberCoroutineScope()

                    TabRow(selectedTabIndex = tabIndex) {
                        tabData.forEachIndexed { index, text ->
                            Tab(selected = tabIndex == index, onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }, text = {
                                Text(text = text)
                            })
                        }
                    }

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


        TextField(
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

        Spacer(modifier = Modifier.height(10.dp))

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
}