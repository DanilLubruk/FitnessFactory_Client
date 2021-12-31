package com.example.fitnessfactory_client.utils

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.fitnessfactory_client.FFApp
import com.example.fitnessfactory_client.R
import java.lang.Exception

object GuiUtils {

    fun showMessage(message: String) {
        val toast: Toast = Toast.makeText(FFApp.instance, message, Toast.LENGTH_LONG)
        setToastSettings(toast)
        toast.show()
    }

    private fun setToastSettings(toast: Toast) {
        try {
            val view: View? = toast.view
            view?.let {
                val margin: Int = ResUtils.getDimen(R.dimen.activity_horizontal_margin)
                val textView: TextView = view.findViewById(android.R.id.message)
                view.setBackgroundColor(ResUtils.getColor(R.color.colorMessageBackground))
                textView.setTextColor(ResUtils.getColor(R.color.black))
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                textView.setGravity(Gravity.CENTER)
                val layoutParams: LinearLayout.LayoutParams = textView.layoutParams as LinearLayout.LayoutParams
                layoutParams.setMargins(margin, margin, margin, margin)
                textView.layoutParams = layoutParams
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}