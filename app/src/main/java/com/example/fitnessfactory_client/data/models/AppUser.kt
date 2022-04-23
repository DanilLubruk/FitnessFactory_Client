package com.example.fitnessfactory_client.data.models

class AppUser {

    companion object {
        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val EMAIL_FIELD = "email"

        fun newValue(id: String, name: String, email: String): AppUser {
            val appUser = AppUser()
            appUser.id = id
            appUser.name = name
            appUser.email = email

            return appUser
        }
    }

    var id: String = ""
    var name: String = ""
    var email: String = ""

    override fun toString(): String =
        name

    fun equals(appUser: AppUser): Boolean =
        id.equals(appUser.id) &&
                name.equals(appUser.name) &&
                email.equals(appUser.email)
}