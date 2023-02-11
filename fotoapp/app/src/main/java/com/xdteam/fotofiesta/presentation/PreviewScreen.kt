package com.xdteam.fotofiesta.presentation

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.xdteam.fotofiesta.R
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun PreviewScreen() {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView: PreviewView = remember { PreviewView(context) }
    val cameraSelector = remember {
        mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA)
    }

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector.value,
            preview
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        Image(
            modifier = Modifier
                .padding(16.dp)
                .size(64.dp)
                .aspectRatio(1f)
                .clickable { },
            painter = painterResource(id = R.drawable.session_start),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            contentDescription = "Rozpocznij sesję"
        )
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}
