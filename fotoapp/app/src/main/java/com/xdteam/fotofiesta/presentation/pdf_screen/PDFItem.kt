package com.xdteam.fotofiesta.presentation.pdf_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xdteam.fotofiesta.R
import com.xdteam.fotofiesta.domain.model.Serie

@Composable
fun PDFItem(serie: Serie, onItemClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(serie.id.toString())
        IconButton(
            onClick = {
                onItemClicked()
            }) {
            Icon(
                painterResource(R.drawable.baseline_insert_drive_file_24),
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
