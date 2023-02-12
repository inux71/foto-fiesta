package com.xdteam.fotofiesta.api.body

import java.io.File

class UploadFilesBody(
    val files: List<File>,
    val serieId: String
) {
}