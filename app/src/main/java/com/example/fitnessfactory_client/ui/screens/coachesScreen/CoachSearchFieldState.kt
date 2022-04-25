package com.example.fitnessfactory_client.ui.screens.coachesScreen

import android.os.Bundle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.AppConsts
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

    fun saveState(savedState: Bundle) {
        savedState.putString(AppConsts.SEARCH_FIELD_KEY, toString())
    }

    companion object {
        fun restoreState(savedState: Bundle): CoachSearchFieldState =
            when (savedState.getString(AppConsts.SEARCH_FIELD_KEY, CoachNameFieldSearch.toString())) {
                CoachNameFieldSearch.toString() -> CoachNameFieldSearch
                CoachEmailFieldSearch.toString() -> CoachEmailFieldSearch
                else -> CoachNameFieldSearch
            }
    }
}
