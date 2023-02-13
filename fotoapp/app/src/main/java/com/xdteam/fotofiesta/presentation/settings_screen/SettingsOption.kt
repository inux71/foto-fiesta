package com.xdteam.fotofiesta.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <TItem> SettingsOption(optionsList: List<TItem>, selectedItem: TItem,  label: String, onChange: (item: TItem) -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var textFilledSize by remember {
        mutableStateOf(Size.Zero)
    }

    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    Column {
        OutlinedTextField(
            value = selectedItem.toString(),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    textFilledSize = it.size.toSize()
                },
            label = {
                Text(text = label)
            },
            trailingIcon = {
                Icon(
                    icon,
                    "",
                    Modifier.clickable {
                        expanded = !expanded
                    }
                )
            },
            readOnly = true,
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.width(with(LocalDensity.current) {
                textFilledSize.width.toDp()
            })
        ) {
            optionsList.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(text = option.toString())
                    },
                    onClick = {
                        onChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
