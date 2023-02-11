package com.xdteam.fotofiesta.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsOption(header: String, placeholder: String) {
    var text by remember {
        mutableStateOf(placeholder)
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(header)
        },
        value = text,
        onValueChange = {
            text = it
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}
