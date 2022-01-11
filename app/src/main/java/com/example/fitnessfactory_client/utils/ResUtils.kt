package com.example.fitnessfactory_client.utils

import android.graphics.drawable.Drawable
import com.example.fitnessfactory_client.FFApp

object ResUtils {

    fun getString(id: Int) =
        FFApp.instance.resources.getString(id)

    fun getDimen(id: Int): Int =
        FFApp.instance.resources.getDimension(id).toInt()

    fun getColor(id: Int) =
        FFApp.instance.resources.getColor(id)

    fun getDrawable(id: Int): Drawable? =
        FFApp.instance.getResources().getDrawable(id)

}