package com.grassterra.fitassist.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.util.Log
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

fun reshapeAndNormalizeImageFile(file: File): ByteBuffer? {
    // Decode the image file into a Bitmap
    val bitmap = BitmapFactory.decodeFile(file.absolutePath) ?: return null

    // Resize the Bitmap to 224x224
    val resizedBitmap = resizeBitmap(bitmap, 224, 224)

    // Normalize the Bitmap and convert it to ByteBuffer
    return convertBitmapToNormalizedByteBuffer(resizedBitmap)
}
fun convertBitmapToNormalizedByteBuffer(bitmap: Bitmap): ByteBuffer {
    val input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder())
    for (y in 0 until 224) {
        for (x in 0 until 224) {
            val px = bitmap.getPixel(x, y)
            // Get channel values from the pixel value.
            val r = Color.red(px)
            val g = Color.green(px)
            val b = Color.blue(px)
            // Normalize channel values to [-1.0, 1.0].
            val rf = (r / 255f)
            val gf = (g / 255f)
            val bf = (b / 255f)

            input.putFloat(rf)
            input.putFloat(gf)
            input.putFloat(bf)
        }
    }
    return input
}

//fun convertBitmapToNormalizedByteBuffer(bitmap: Bitmap): ByteBuffer {
//    val inputShape = intArrayOf(1, 224, 224, 3) // Assuming input shape is [1, 224, 224, 3]
//    val input = ByteBuffer.allocateDirect(inputShape[0] * inputShape[1] * inputShape[2] * inputShape[3] * 4).order(ByteOrder.nativeOrder())
//
//    // Resize the bitmap
//    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputShape[1], inputShape[2], true)
//
//    for (y in 0 until inputShape[1]) {
//        for (x in 0 until inputShape[2]) {
//            val px = resizedBitmap.getPixel(x, y)
//
//            // Get channel values from the pixel value.
//            val r = Color.red(px)
//            val g = Color.green(px)
//            val b = Color.blue(px)
//
//            // Normalize channel values to [-1.0, 1.0].
//            val rf = (r / 127.5f) - 1.0f
//            val gf = (g / 127.5f) - 1.0f
//            val bf = (b / 127.5f) - 1.0f
//
//            input.putFloat(rf)
//            input.putFloat(gf)
//            input.putFloat(bf)
//        }
//    }
//    return input
//}

fun logBitmapDimensions(bitmap: Bitmap) {
    Log.d("ResizedBitmap", "Width: ${bitmap.width}, Height: ${bitmap.height}")
}


fun printByteBuffer(byteBuffer: ByteBuffer) {
    // Create a copy of the ByteBuffer to avoid changing the position of the original buffer
    val bufferCopy = byteBuffer.asFloatBuffer()
    val floatArray = FloatArray(bufferCopy.remaining())
    bufferCopy.get(floatArray)
    Log.d("in","in")
    for (i in floatArray.indices step 3) {
        Log.d("ByteBuffer","r: ${floatArray[i]}, g: ${floatArray[i+1]}, b: ${floatArray[i+2]}")
    }
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