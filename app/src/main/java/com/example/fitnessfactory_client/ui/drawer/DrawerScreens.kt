package com.example.fitnessfactory_client.ui.drawer

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.ui.screens.Screens
import com.example.fitnessfactory_client.utils.ResUtils

sealed class DrawerScreens(val title: String, val navRoute: String) {

    object Home : DrawerScreens(
        ResUtils.getString(R.string.title_home_screen),
        Screens.HOME_SCREEN)

    object MySessions : DrawerScreens(
        ResUtils.getString(R.string.title_sessions_screen),
        Screens.MY_SESSIONS_SCREEN)

    object Coaches : DrawerScreens(
        ResUtils.getString(R.string.title_coaches_screen),
        Screens.COACHES_SCREEN)

    object SessionTypes : DrawerScreens(
        ResUtils.getString(R.string.title_session_types_screen),
        Screens.SESSION_TYPES_SCREEN)

    companion object {
        fun getDrawerScreens(): List<DrawerScreens> =
            listOf(
                Home,
                MySessions,
                Coaches,
                SessionTypes
            )
    }
}
