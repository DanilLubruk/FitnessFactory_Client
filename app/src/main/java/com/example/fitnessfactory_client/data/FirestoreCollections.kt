package com.example.fitnessfactory_client.data

object FirestoreCollections {

    const val organisationDataCollectionValue: String = "organisationData"
    const val appDataCollectionValue: String = "appData"
    private const val usersCollection: String = "users"
    private const val usersAccessCollection: String = "usersAccess"
    private const val ownersCollection: String = "owners"

    fun getBaseCollection(): String =
        ownersCollection +
                "/" +
                AppPrefs.gymOwnerId().value +
                "/" +
                AppPrefs.gymOwnerId().value

    fun getOwnersCollection(): String =
        ownersCollection

    fun getUsersCollection(): String =
        appDataCollectionValue +
                "/" +
                usersCollection +
                "/" +
                usersCollection

    fun getUsersAccessCollection(): String =
        appDataCollectionValue +
                "/" +
                usersAccessCollection +
                "/" +
                usersCollection
}