package com.example.fitnessfactory_client.data.models

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.utils.ResUtils
import kotlin.properties.Delegates

class SessionType(doFilter: Boolean = true): FilterEntity(doFilter = doFilter) {

    companion object {
        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val PEOPLE_AMOUNT_FIELD = "peopleAmount"
        const val PRICE_FIELD = "price"

        fun getNoFilterEntity(): SessionType {
            val sessionType = SessionType(doFilter = false)
            sessionType.toStringField = ResUtils.getString(R.string.caption_no_filter)

            return sessionType
        }
    }

    lateinit var id: String
    lateinit var name: String
    private var toStringField: String
    set(value) {
        name = value
    }
    get() = name
    var peopleAmount by Delegates.notNull<Int>()
    var price by Delegates.notNull<Float>()

    override fun toString(): String =
        toStringField
}