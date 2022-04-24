package com.example.fitnessfactory_client.data.models

class AccessEntry {

    companion object {
        const val OWNER_ID_FIELD = "ownerId"
        const val USERS_ID_FIELD = "userId"
    }

    lateinit var ownerId: String
    lateinit var usersId: String
}