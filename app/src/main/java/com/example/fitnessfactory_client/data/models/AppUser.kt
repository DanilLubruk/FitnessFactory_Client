package com.example.fitnessfactory_client.data.models

class AppUser() {

    companion object {
        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val EMAIL_FIELD = "email"
    }

    lateinit var id: String
    lateinit var name: String
    lateinit var email: String
}