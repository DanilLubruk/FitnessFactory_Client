package com.example.fitnessfactory_client.data.beans

import androidx.compose.runtime.saveable.Saver
import com.example.fitnessfactory_client.ui.components.HomeScreenCalendarView
import java.time.LocalDate

class TopBarAction(var actionName: String, var action: () -> Unit) {
}