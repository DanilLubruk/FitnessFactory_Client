package com.example.fitnessfactory_client.ui.screens.gymsScreen

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.ui.screens.coachesScreen.CoachSearchFieldState
import com.example.fitnessfactory_client.utils.ResUtils

sealed class GymSearchFieldState {
    abstract fun getSearchField(gym: Gym): String

    object GymNameFieldSearch : GymSearchFieldState() {
        override fun getSearchField(gym: Gym) = gym.name.toLowerCase(Locale.current)

        override fun toString(): String = ResUtils.getString(R.string.caption_name)
    }

    object GymAddressFieldSearch : GymSearchFieldState() {
        override fun getSearchField(gym: Gym) = gym.address.toLowerCase(Locale.current)

        override fun toString(): String = ResUtils.getString(R.string.caption_address)
    }
}