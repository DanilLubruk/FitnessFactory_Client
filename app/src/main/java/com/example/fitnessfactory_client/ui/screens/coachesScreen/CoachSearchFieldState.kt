package com.example.fitnessfactory_client.ui.screens.coachesScreen

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.utils.ResUtils

sealed class CoachSearchFieldState {
    abstract fun getSearchField(appUser: AppUser): String

    object CoachNameFieldSearch : CoachSearchFieldState() {
        override fun getSearchField(appUser: AppUser) = appUser.name.toLowerCase(Locale.current)

        override fun toString(): String = ResUtils.getString(R.string.caption_name)
    }

    object CoachEmailFieldSearch : CoachSearchFieldState() {
        override fun getSearchField(appUser: AppUser) = appUser.email.toLowerCase(Locale.current)

        override fun toString(): String = ResUtils.getString(R.string.caption_email)
    }
}
