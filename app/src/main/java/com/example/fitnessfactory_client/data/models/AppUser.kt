package com.example.fitnessfactory_client.data.models

class AppUser {

    companion object {
        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val EMAIL_FIELD = "email"
    }

    var id: String = ""
    var name: String = ""
    var email: String = ""

    override fun toString(): String =
        name
}