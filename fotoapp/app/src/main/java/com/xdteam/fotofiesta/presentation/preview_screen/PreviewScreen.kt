package com.xdteam.fotofiesta.presentation.preview_screen

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun PreviewScreen(
    viewModel: PreviewScreenViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit,
    onPDFClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

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

    LaunchedEffect(state.lensFacing) {
        val cameraProvider = context.getCameraProvider()

        val cameraSelector = CameraSelector.Builder().requireLensFacing(state.lensFacing).build()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    LaunchedEffect(true) {
        viewModel.countdownFlow.collect {
            when (it) {
                PreviewScreenEvent.SeriesStarted -> {}
                PreviewScreenEvent.SeriesFinished -> {
                    takePhoto(imageCapture, context.mainExecutor, context.contentResolver) { uri ->
                        uri.path?.let { pic ->
                            var cursor: Cursor? = null

                            try {
                                val proj = arrayOf(MediaStore.Images.Media.DATA)
                                cursor = context.contentResolver.query(uri, proj, null, null, null)!!
                                val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                                cursor.moveToFirst()

                                viewModel.addPicture(cursor.getString(index))
                            } finally {
                                cursor?.close()
                            }
                        }
                    }

                    singlePhotoDonePlayer.start()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        if (state.timerState == TimerState.STARTED) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    String.format("%.1f", state.timerValue),
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black)
                )

                Text(
                    "${state.picturesTaken}/${state.numberOfPhotos}",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                )
            }
        }

        if (state.timerState == TimerState.STARTED) {
            IconButton(
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
                    .background(
                        color = Color.Red,
                        shape = CircleShape
                    ),
                onClick = {
                    //viewModel.stopSeries()
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
                        if (state.timerState == TimerState.IDLE) {
                            viewModel.startTimer()
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
                enabled = state.timerState == TimerState.IDLE
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
                        viewModel.flipCamera()
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

private fun Context.getOutputDirectory(): File {
    val mediaDir = externalMediaDirs.firstOrNull()?.let {
        File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else filesDir
}

private suspend fun takePhoto(
    imageCapture: ImageCapture,
    executor: Executor,
    contentResolver: ContentResolver,
    onImageCaptured: (Uri) -> Unit = {}
)  {
    val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
        .format(System.currentTimeMillis())

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "$name.jpg")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FotoFiesta")
    }

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                Log.e("[FF]", "Take photo error:", exception)
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onImageCaptured(outputFileResults.savedUri ?: Uri.EMPTY)
            }
        }
    )
}
