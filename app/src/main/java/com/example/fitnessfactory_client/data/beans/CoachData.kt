package com.example.fitnessfactory_client.data.beans

import com.example.fitnessfactory_client.R
import com.example.fitnessfactory_client.data.models.AppUser
import com.example.fitnessfactory_client.data.models.FilterEntity
import com.example.fitnessfactory_client.data.models.Gym
import com.example.fitnessfactory_client.data.models.Personnel
import com.example.fitnessfactory_client.utils.ResUtils

class CoachData(private var coachUser: AppUser, private var coach: Personnel, doFilter: Boolean = true) :
    FilterEntity(doFilter = doFilter) {

    companion object {
        fun getNoFilterEntity(): CoachData {
            val coachData = CoachData(coach = Personnel(), coachUser = AppUser(), doFilter = false)
            coachData.toStringField = ResUtils.getString(R.string.caption_no_filter)

            return coachData
        }
    }

    var id: String = ""
    private set
    get() = coach.id

    var name: String = ""
    private set
    get() = coachUser.name

    private var toStringField: String
    set(value) {
        coachUser.name = value
    }
    get() = coachUser.name


    override fun toString(): String =
        toStringField
}