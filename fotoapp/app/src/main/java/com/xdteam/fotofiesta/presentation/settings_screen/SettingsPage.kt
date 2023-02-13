package com.xdteam.fotofiesta.presentation.settings_screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xdteam.fotofiesta.R
import com.xdteam.fotofiesta.presentation.SettingsOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = { }
) {
    val state by viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "")
                    }
                },
                title = {
                    Text(stringResource(R.string.settings_page_header), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                }
            )
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsOption(
                optionsList = (1..10).toList(),
                selectedItem = state.delay,
                label = stringResource(R.string.delay_option_header)
            ) { delay ->
                viewModel.onDelayChanged(delay)
            }
            SettingsOption(
                optionsList = (2..5).toList(),
                selectedItem = state.numberOfPhotos,
                label = stringResource(R.string.serie_photo_size)
            ) { numberOfPhotos ->
                viewModel.onNumberOfPhotosChanged(numberOfPhotos)
            }
        }
    }

}
