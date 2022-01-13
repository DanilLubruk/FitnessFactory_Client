package com.example.fitnessfactory_client.data.models

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.utils.ResUtils

class Gym(doFilter: Boolean = true): FilterEntity(doFilter = doFilter) {

    companion object {
        const val ID_FIELD = "id"
        const val NAME_FIELD = "name"
        const val ADDRESS_FIELD = "address"

        fun getNoFilterEntity(): Gym {
            val gym = Gym(doFilter = false)
            gym.toStringField = ResUtils.getString(R.string.caption_no_filter)

            return gym
        }
    }

    var id: String = ""
    var name: String = ""
    private var toStringField: String
    set(value) {
        name = value
    }
    get() = name
    lateinit var address: String

    override fun toString(): String =
        toStringField
}