package com.example.fitnessfactory_client.data.models

class UsersAccessEntry() {

    companion object {
        const val USER_ID_FIELD = "userId"
        const val OWNER_ID_FIELD = "ownerId"
    }

    lateinit var userId: String
    lateinit var ownerId: String
}