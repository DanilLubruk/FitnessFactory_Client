package com.example.fitnessfactory_client.ui.screens.gymsScreen

import android.os.Bundle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.AppConsts
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

    fun saveState(savedState: Bundle) {
        savedState.putString(AppConsts.SEARCH_FIELD_KEY, toString())
    }

    companion object {
        fun restoreState(savedState: Bundle): GymSearchFieldState =
            when (savedState.getString(AppConsts.SEARCH_FIELD_KEY, GymNameFieldSearch.toString())) {
                GymNameFieldSearch.toString() -> GymNameFieldSearch
                GymAddressFieldSearch.toString() -> GymAddressFieldSearch
                else -> GymNameFieldSearch
            }
    }
}