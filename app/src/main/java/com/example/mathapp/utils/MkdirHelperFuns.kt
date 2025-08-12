package com.example.mathapp.utils

import android.content.Context
import java.io.File

fun Context.createPrivateDir(): File {
    val directory = getExternalFilesDir(null)
    val file = File(directory, PDF_DIRECTORY)
    if (file.exists().not()) file.mkdirs()
    return file
}