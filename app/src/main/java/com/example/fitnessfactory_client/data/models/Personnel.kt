package com.example.fitnessfactory_client.data.models

class Personnel() {

    companion object {
        const val USER_ID_FIELD = "userId"
        const val GYMS_ARRAY_FIELD = "gymsIds"
    }

    var userId: String = ""
    var gymsIds: List<String>? = ArrayList()
}