package com.example.myapplication3333.srvices



import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.ByteArrayOutputStream

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder


class NeurelNetService {

    companion object {

        fun getModel(context: Context): ImageClassifier {
            var inputStream: InputStream? = null
            try {
                inputStream = context.getAssets().open("model4.tflite")
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val buffer = ByteArray(8192)
            var bytesRead: Int
            val output = ByteArrayOutputStream()
            try {
                while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
                    output.write(buffer, 0, bytesRead)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val file: ByteArray = output.toByteArray()
            val bb: ByteBuffer = ByteBuffer.allocateDirect(file.size)
            bb.order(ByteOrder.nativeOrder())
            bb.put(file)
            val baseOptionsBuilder = BaseOptions.builder().setNumThreads(3)
            baseOptionsBuilder.useNnapi()
            val options: ImageClassifier.ImageClassifierOptions =
                ImageClassifier.ImageClassifierOptions.builder()
                    .setMaxResults(3).setBaseOptions(baseOptionsBuilder.build()).build()
            return ImageClassifier.createFromBufferAndOptions(
                bb, options
            )
        }

        fun getTensorImgFromPath(path: String): TensorImage? {
            val bmOptions = BitmapFactory.Options()
            var bitmap: Bitmap? = BitmapFactory.decodeFile(path, bmOptions) ?: return null
            var tensorImage = TensorImage(DataType.UINT8)
            tensorImage.load(bitmap)
            return tensorImage
        }

    }

}