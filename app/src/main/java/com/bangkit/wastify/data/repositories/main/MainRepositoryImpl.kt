package com.bangkit.wastify.data.repositories.main

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import com.bangkit.wastify.data.db.WastifyDao
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.model.Classification
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.ml.WastifyModel
import com.bangkit.wastify.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.single
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val wastifyDao: WastifyDao,
    private val model: WastifyModel
) : MainRepository {

    override fun getTypes() = wastifyDao.getTypes()
    override fun getCategories() = wastifyDao.getCategories()
    override fun getArticles() = wastifyDao.getArticles()

    override suspend fun getTypeById(id: Int) = wastifyDao.getTypeById(id)
    override suspend fun getCategoryById(id: Int) = wastifyDao.getCategoryById(id)

    override suspend fun classifyWaste(image: Bitmap): UiState<Classification> {
        return try {
            val imageSize = 320

            val dimension: Int = image.width.coerceAtMost(image.height)
            val imgTemp = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            val scaledImg = Bitmap.createScaledBitmap(imgTemp, 320, 320, false)

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)

            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            val intValues = IntArray(imageSize * imageSize)
            scaledImg.getPixels(intValues, 0, scaledImg.width, 0, 0, scaledImg.width, scaledImg.height)
            var pixel = 0
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val value = intValues[pixel++] // RGB
                    byteBuffer.putFloat((value shr 16 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((value shr 8 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((value and 0xFF) * (1f / 1))
                }
            }

            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val confidences = outputFeature0.floatArray
            // find the index of the class with the biggest confidence.
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }

            val result = generateResult(scaledImg, maxPos, maxConfidence.toDouble())
            UiState.Success(result)
        } catch (e: Exception) {
            UiState.Failure(e.message)
        }
    }

    private fun generateResult(
        img: Bitmap,
        maxPos: Int,
        maxConfidence: Double
    ): Classification {
        // Result
        val classes = arrayOf("battery", "biological", "brown-glass", "cardboard", "clothes", "green-glass", "metal", "paper", "plastic", "shoes", "trash", "white-glass")
        val result = classes[maxPos]

        val categoryId = when(maxPos) {
            0 -> 1
            1 -> 2
            2, 5, 11 -> 3
            3 -> 4
            4 -> 5
            6 -> 6
            7 -> 7
            8 -> 8
            9 -> 5
            10 -> 5
            else -> 2
        }
        val typeId = when(categoryId) {
            1,2,3,4 -> categoryId
            else -> 5
        }
        val recyclable = false

        return Classification(
            img, result, maxConfidence, typeId, categoryId, recyclable
        )
    }
}