package com.example.fitnessfactory_client.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.fitnessfactory_client.R

object DataScreenField {

    @Composable
    fun DataScreenField(content: String, hint: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            OutlinedTextField(
                value = content,
                onValueChange = {},
                label = { Text(hint) },
                textStyle = MaterialTheme.typography.body1,
                colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable(onClick = {})
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}