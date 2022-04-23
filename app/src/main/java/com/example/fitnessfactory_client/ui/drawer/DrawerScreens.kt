package com.example.fitnessfactory_client.ui.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.ui.screens.Screens
import com.example.fitnessfactory_client.utils.ResUtils

sealed class DrawerScreens(val icon: ImageVector, val title: String, val navRoute: String) {

    object Home : DrawerScreens(
        Icons.Filled.Home,
        ResUtils.getString(R.string.title_home_screen),
        Screens.HOME_SCREEN
    )

    object MySessions : DrawerScreens(
        Icons.Filled.FormatListBulleted,
        ResUtils.getString(R.string.title_sessions_screen),
        Screens.MY_SESSIONS_SCREEN
    )

    object Coaches : DrawerScreens(
        Icons.Filled.Sports,
        ResUtils.getString(R.string.title_coaches_screen),
        Screens.COACHES_SCREEN
    )

    object SessionTypes : DrawerScreens(
        Icons.Filled.SportsBasketball,
        ResUtils.getString(R.string.title_session_types_screen),
        Screens.SESSION_TYPES_SCREEN
    )

    object Gyms : DrawerScreens(
        Icons.Filled.StoreMallDirectory,
        ResUtils.getString(R.string.caption_gyms),
        Screens.GYMS_SCREEN
    )

    object PersonalInfo : DrawerScreens(
        Icons.Filled.Info,
        ResUtils.getString(R.string.caption_personal_info),
        Screens.PERSONAL_INFO_SCREEN
    )

    companion object {
        fun getDrawerScreensGrouped(): List<List<DrawerScreens>> = listOf(
            listOf(Home),
            listOf(MySessions),
            listOf(Coaches, Gyms, SessionTypes),
            listOf(PersonalInfo)
        )
    }
}
