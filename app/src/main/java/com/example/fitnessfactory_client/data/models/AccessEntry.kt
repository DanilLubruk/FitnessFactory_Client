package com.example.fitnessfactory_client.data.models

class AccessEntry {

    companion object {
        const val OWNER_ID_FIELD = "ownerId"
        const val USERS_EMAIL_FIELD = "userEmail"
    }

    lateinit var ownerId: String
    lateinit var usersEmail: String
}