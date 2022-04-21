package com.example.fitnessfactory_client.data.beans

import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.fitnessfactory_client.ui.components.HomeScreenCalendarView
import java.time.LocalDate

class TopBarAction(
    var actionName: String,
    var image: ImageVector,
    var imageTint: Color = Color.White,
    var action: () -> Unit,
) {
}