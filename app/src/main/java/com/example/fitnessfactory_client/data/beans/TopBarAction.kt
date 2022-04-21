package com.example.fitnessfactory_client.data.beans

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

class TopBarAction(
    var actionName: String,
    var image: ImageVector,
    var imageTint: Color = Color.White,
    var action: () -> Unit,
) {
}