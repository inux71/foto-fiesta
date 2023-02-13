package com.xdteam.fotofiesta.presentation.preview_screen

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.xdteam.fotofiesta.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun PreviewScreen(
    viewModel: PreviewScreenViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit,
    onPDFClick: () -> Unit
) {
    val state by viewModel.state

    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val imageCapture = ImageCapture.Builder().build()
    val previewView: PreviewView = remember { PreviewView(context) }

    val singlePhotoDonePlayer = remember {
        MediaPlayer.create(context, R.raw.single).also {
            it.isLooping = false
        }
    }
    val serieFinishedPlayer = remember {
        MediaPlayer.create(context, R.raw.full).also {
            it.isLooping = false
        }
    }
    val delayReachedPlayer = remember {
        MediaPlayer.create(context, R.raw.delay).also {
            it.isLooping = false
        }
    }

    LaunchedEffect(lensFacing, state.cameraSelector) {
        val cameraProvider = context.getCameraProvider()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            state.cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        if (state.timerState == TIMER_STATE.STARTED) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    state.countDownTime.toString(),
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black)
                )

                Text(
                    "${state.picturesTaken}/5",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                )
            }
        }

        if (state.timerState == TIMER_STATE.STARTED) {
            IconButton(
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
                    .background(
                        color = Color.Red,
                        shape = CircleShape
                    ),
                onClick = {
                    viewModel.stopSeries()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = "Anuluj",
                    tint = Color.White
                )
            }
        } else {
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .clickable {
                        if (state.timerState == TIMER_STATE.IDLE) {
                            viewModel.startSeries(
                                onSinglePhotoDone = {
                                    singlePhotoDonePlayer.also {
                                        it.start()
                                    }
                                },
                                onSerieFinished = {
                                    serieFinishedPlayer.also {
                                        it.start()
                                    }
                                }
                            )
                        }
                    },
                painter = painterResource(id = R.drawable.session_start),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                contentDescription = "Rozpocznij sesję"
            )

            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                onClick = onSettingsClick,
                enabled = state.timerState == TIMER_STATE.IDLE
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Ustawienia",
                    tint = Color.White
                )
            }

            Column(modifier = Modifier.align(Alignment.BottomEnd)) {
                IconButton(
                    modifier = Modifier
                        .padding(24.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    onClick = onPDFClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_insert_drive_file_24),
                        contentDescription = "Wyświetl listę PDF",
                        tint = Color.White
                    )
                }

                IconButton(
                    modifier = Modifier
                        .padding(24.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    onClick = {
                        Log.i("[FF]", "Zmieniam kamerę")
                        viewModel.setCameraSelector(
                            if (state.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else {
                                CameraSelector.DEFAULT_BACK_CAMERA
                            }
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera_flip),
                        contentDescription = "Zmień kamerę",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

private fun takePhoto(
    filenameFormat: String,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {

    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.GERMAN).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            Log.e("kilo", "Take photo error:", exception)
            onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(photoFile)
            onImageCaptured(savedUri)
        }
    })
}