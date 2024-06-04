package com.grassterra.fitassist.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.grassterra.fitassist.R
import com.grassterra.fitassist.ml.GymClassification
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResults: Int = 7,
    val modelName: String = "gym_classification.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {

    private var gymClassifier: GymClassification? = null

    init {
        setupGymClassifier()
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: TensorBuffer)
    }

    private fun setupGymClassifier() {
        try {
            gymClassifier = GymClassification.newInstance(context)
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        if (gymClassifier == null) {
            setupGymClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.FLOAT32))
            .build()

        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)

        bitmap?.let {
            val tensorImage = TensorImage.fromBitmap(bitmap)
            val processedImage = imageProcessor.process(tensorImage)

            // Convert the TensorImage to TensorBuffer
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(processedImage.buffer)

            // Run inference
            val outputs = gymClassifier?.process(inputFeature0)
            val outputFeature0 = outputs?.outputFeature0AsTensorBuffer

            outputFeature0?.let { buffer ->
                classifierListener?.onResults(buffer)
            }
        }
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}
