package com.example.fitnessfactory_client.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

object DropDownList {

    @Composable
    fun <T> DropDownList(
        isToogled: Boolean = false,
        items: List<T>,
        selectItem: (T) -> Unit,
        setToogle: (Boolean) -> Unit
    ) {
        DropdownMenu(
            expanded = isToogled,
            onDismissRequest = { }) {
            items.forEach {
                DropdownMenuItem(
                    onClick = {
                        setToogle(false)
                        selectItem(it)
                    }) {
                    Text(text = it.toString())
                }
            }
        }

    }
}