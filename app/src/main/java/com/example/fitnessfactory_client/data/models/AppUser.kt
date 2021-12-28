package com.example.fitnessfactory_client.data.models

class AppUser() {

    companion object {
        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val EMAIL_FIELD = "email"
    }

    private lateinit var id: String get set
    private lateinit var name: String get set
    private lateinit var email: String get set
}