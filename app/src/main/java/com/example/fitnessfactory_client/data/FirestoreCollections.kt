package com.example.fitnessfactory_client.data

object FirestoreCollections {

    const val organisationDataCollectionValue: String = "organisationData"
    const val appDataCollectionValue: String = "appData"
    private const val usersCollection: String = "users"
    private const val clientsAccessCollection: String = "clientsAccess"
    private const val adminsAccessCollection: String = "adminsAccess"
    private const val coachesAccessCollection: String = "coachesAccess"
    private const val ownersCollection: String = "owners"
    private const val sessionsCollection: String = "sessions"
    private const val sessionTypesCollection: String = "sessionTypes"
    private const val gymsCollection: String = "gyms"
    private const val clientsCollection: String = "clients"
    private const val coachesCollection: String = "coaches"

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

    fun getClientsAccessCollection(): String =
        appDataCollectionValue +
                "/" +
                clientsAccessCollection +
                "/" +
                clientsAccessCollection

    fun getAdminsAccessCollection(): String =
        appDataCollectionValue +
                "/" +
                adminsAccessCollection +
                "/" +
                adminsAccessCollection

    fun getCoachesAccessCollection(): String =
        appDataCollectionValue +
                "/" +
                coachesAccessCollection +
                "/" +
                coachesAccessCollection

    fun getSessionsCollection(): String =
        getBaseCollection() +
                "/" +
                sessionsCollection +
                "/" +
                sessionsCollection

    fun getSessionTypesCollection(): String =
        getBaseCollection() +
                "/" +
                sessionTypesCollection +
                "/" +
                sessionTypesCollection

    fun getOwnerGymsCollection(): String =
        getBaseCollection() +
                "/" +
                gymsCollection +
                "/" +
                gymsCollection

    fun getOwnerClientsCollection(): String =
        getBaseCollection() +
                "/" +
                clientsCollection +
                "/" +
                clientsCollection

    fun getClientSessionsCollection(clientId: String): String =
        getOwnerClientsCollection() +
                "/" +
                clientId +
                "/" +
                sessionsCollection

    fun getOwnerCoachesCollection(): String =
        getBaseCollection() +
                "/" +
                coachesCollection +
                "/" +
                coachesCollection
}