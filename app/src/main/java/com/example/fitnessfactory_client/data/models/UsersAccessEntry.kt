package com.example.fitnessfactory_client.data.models

class UsersAccessEntry() {

    companion object {
        const val USER_EMAIL_FIELD = "userEmail"
        const val OWNER_ID_FIELD = "ownerId"
    }

    lateinit var userEmail: String
    lateinit var ownerId: String
}