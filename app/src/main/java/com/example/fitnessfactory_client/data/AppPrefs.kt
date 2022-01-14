package com.example.fitnessfactory_client.data

import com.tiromansev.prefswrapper.typedprefs.StringPreference

object AppPrefs {

    fun gymOwnerId(): StringPreference =
        StringPreference
            .builder("gym_owner_id")
            .setDefaultValue("")
            .build()

    fun currencySing(): StringPreference =
        StringPreference
            .builder("currency_sign_pref")
            .setDefaultValue("₽")
            .build()
}