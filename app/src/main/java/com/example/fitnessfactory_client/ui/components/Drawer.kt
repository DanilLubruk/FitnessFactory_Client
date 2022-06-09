package com.example.fitnessfactory_client.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.sharp.Logout
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.ui.drawer.DrawerScreens
import com.example.fitnessfactory_client.utils.ResUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

object Drawer {

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    @Composable
    fun Drawer(
        modifier: Modifier = Modifier,
        onDestinationClicked: (route: DrawerScreens) -> Unit,
        logout: () -> Unit
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.White),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = colorResource(id = R.color.royalBlue))
            ) {
                Icon(
                    modifier = Modifier.padding(top = 16.dp),
                    imageVector = Icons.Filled.FitnessCenter,
                    contentDescription = "",
                    tint = Color.White,
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = ResUtils.getString(R.string.app_name),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            DrawerScreens.getDrawerScreensGrouped().forEach { group ->
                group.forEach { screen ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable {
                                onDestinationClicked(screen)
                            },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(start = 16.dp),
                            imageVector = screen.icon,
                            contentDescription = screen.title,
                            tint = Color.Black,
                        )
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = screen.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )
                    }
                }
                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                IconButton(onClick = { logout() }) {
                    Icon(Icons.Sharp.Logout, contentDescription = "", tint = Color.Black)
                }
            }
        }
    }
}