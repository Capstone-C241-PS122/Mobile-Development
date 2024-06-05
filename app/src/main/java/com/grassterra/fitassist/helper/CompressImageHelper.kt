package com.grassterra.fitassist.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun uriToFile(context: Context, uri: Uri): File? {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    inputStream ?: return null

    val file = File(context.cacheDir, "temp_image.jpg")
    file.createNewFile()

    FileOutputStream(file).use { outputStream ->
        inputStream.copyTo(outputStream)
    }

    return file
}

fun compressImageFile(file: File, maxSizeMB: Int = 1): File? {
    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
    val compressedByteArray = compressBitmap(bitmap, maxSizeMB)

    val compressedFile = File.createTempFile("compressed_image", ".jpg")
    FileOutputStream(compressedFile).use { outputStream ->
        outputStream.write(compressedByteArray)
    }

    return compressedFile
}

fun compressBitmap(bitmap: Bitmap, maxSizeMB: Int = 1): ByteArray {
    val stream = ByteArrayOutputStream()
    var quality = 100
    var byteArray: ByteArray

    do {
        stream.reset()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        byteArray = stream.toByteArray()
        quality -= 5
    } while (byteArray.size > maxSizeMB * 1024 * 1024 && quality > 0)

    return byteArray
}