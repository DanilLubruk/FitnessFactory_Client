package com.example.fitnessfactory_client.data.models

class Gym {

    companion object {
        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val ADDRESS_FIELD = "address"
    }

    lateinit var id: String
    lateinit var name: String
    lateinit var address: String

    override fun toString(): String =
        name
}