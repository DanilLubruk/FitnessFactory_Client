package com.example.fitnessfactory_client.data.models

import kotlin.properties.Delegates

class SessionType {

    companion object {
        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val PEOPLE_AMOUNT_FIELD = "peopleAmount"
        const val PRICE_FIELD = "price"
    }

    lateinit var id: String
    lateinit var name: String
    var peopleAmount by Delegates.notNull<Int>()
    var price by Delegates.notNull<Float>()

}