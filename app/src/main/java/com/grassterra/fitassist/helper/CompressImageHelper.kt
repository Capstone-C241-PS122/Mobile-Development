package com.grassterra.fitassist.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

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

fun resizeImageFile(file: File): File? {
    // Step 1: Decode the image file into a Bitmap
    val bitmap = BitmapFactory.decodeFile(file.absolutePath) ?: return null

    // Step 2: Resize the Bitmap to 224x224
    val resizedBitmap = resizeBitmap(bitmap, 224, 224)

    // Step 3: Save the resized bitmap to a file
    val resizedFile = File.createTempFile("resized_image", ".jpg")
    saveBitmapToFile(resizedBitmap, resizedFile)

    return resizedFile
}

fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, width, height, true)
}

fun saveBitmapToFile(bitmap: Bitmap, file: File) {
    try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun reshapeAndNormalizeImageFile(file: File): Array<Array<Array<FloatArray>>>? {
    // Decode the image file into a Bitmap
    val bitmap = BitmapFactory.decodeFile(file.absolutePath) ?: return null

    // Resize the Bitmap to 224x224
    val resizedBitmap = resizeBitmap(bitmap, 224, 224)

    // Normalize the Bitmap and convert it to the desired tensor format
    return convertBitmapToNormalizedTensor(resizedBitmap)
}

fun convertBitmapToNormalizedTensor(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
    val tensor = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }
    for (y in 0 until 224) {
        for (x in 0 until 224) {
            val pixel = bitmap.getPixel(x, y)
            tensor[0][y][x][0] = ((pixel shr 16) and 0xFF) / 127.5f - 1 // Red channel
            tensor[0][y][x][1] = ((pixel shr 8) and 0xFF) / 127.5f - 1 // Green channel
            tensor[0][y][x][2] = (pixel and 0xFF) / 127.5f - 1 // Blue channel
        }
    }
    return tensor
}

//fun compressAndResizeImageFile(file: File, maxSizeMB: Int = 1): File? {
//    // Step 1: Compress the image file
//    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
//    val compressedByteArray = compressBitmap(bitmap, maxSizeMB)
//
//    val compressedFile = File.createTempFile("compressed_image", ".jpg")
//    FileOutputStream(compressedFile).use { outputStream ->
//        outputStream.write(compressedByteArray)
//    }
//
//    // Step 2: Resize the compressed image to 224x224
//    val resizedBitmap = BitmapFactory.decodeByteArray(compressedByteArray, 0, compressedByteArray.size)
//        ?.let { resizeBitmap(it, 224, 224) } ?: return null
//
//    // Step 3: Save the resized bitmap to a file
//    val resizedFile = File.createTempFile("resized_image", ".jpg")
//    saveBitmapToFile(resizedBitmap, resizedFile)
//
//    return resizedFile
//}
//
//fun compressBitmap(bitmap: Bitmap, maxSizeMB: Int = 1): ByteArray {
//    val stream = ByteArrayOutputStream()
//    var quality = 100
//    var byteArray: ByteArray
//
//    do {
//        stream.reset()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
//        byteArray = stream.toByteArray()
//        quality -= 5
//    } while (byteArray.size > maxSizeMB * 1024 * 1024 && quality > 0)
//
//    return byteArray
//}
//
//fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
//    return Bitmap.createScaledBitmap(bitmap, width, height, true)
//}
//
//fun saveBitmapToFile(bitmap: Bitmap, file: File) {
//    try {
//        FileOutputStream(file).use { out ->
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
//        }
//    } catch (e: IOException) {
//        e.printStackTrace()
//    }
//}