package com.example.fitnessfactory_client.data

object FirestoreCollections {

    private val appDataCollection: String = "appData"
    private val usersCollection: String = "users"

    fun getBaseCollection(): String =
        AppPrefs.gymOwnerId().value

    fun getOwnersCollection(): String =
        ""

    fun getUsersCollection(): String =
        appDataCollection +
                "/" +
                usersCollection +
                "/" +
                usersCollection
}