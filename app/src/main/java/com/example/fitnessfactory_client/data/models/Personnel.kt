package com.example.fitnessfactory_client.data.models

class Personnel() {

    companion object {
        const val ID_FIELD = "id"
        const val USER_EMAIL_FIELD = "userEmail"
        const val GYMS_ARRAY_FIELD = "gymsIds"
    }

    var id: String = ""
    lateinit var userEmail: String
    var gymsIds: List<String> = ArrayList()
}