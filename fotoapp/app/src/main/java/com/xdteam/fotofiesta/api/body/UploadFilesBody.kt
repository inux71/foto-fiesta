package com.xdteam.fotofiesta.api.body

import java.io.File

data class UploadFilesBody(
    val files: List<File>,
    val serieId: String
)
