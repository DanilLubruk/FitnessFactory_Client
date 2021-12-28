package com.example.fitnessfactory_client.data.firestoreCollections

import com.example.fitnessfactory_client.data.AppPrefs

object BaseCollection {

    fun getBaseCollection(): String =
        AppPrefs.gymOwnerId().value

}