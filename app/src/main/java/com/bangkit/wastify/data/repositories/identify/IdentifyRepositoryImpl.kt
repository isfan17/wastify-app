package com.bangkit.wastify.data.repositories.identify

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import com.bangkit.wastify.data.db.dao.ResultDao
import com.bangkit.wastify.data.model.Classification
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.data.model.asEntityModel
import com.bangkit.wastify.ml.WastifyModel
import com.bangkit.wastify.utils.Helper.getCurrentFormattedDate
import com.bangkit.wastify.utils.UiState
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

class IdentifyRepositoryImpl @Inject constructor(
    private val model: WastifyModel,
    private val resultDao: ResultDao,
): IdentifyRepository {

    override suspend fun storeResult(result: Result) = resultDao.insertResult(result.asEntityModel())

    override suspend fun deleteResult(result: Result) {
        val resultEntity = result.asEntityModel()
        resultEntity.id = result.id!!
        resultDao.deleteResult(resultEntity)
    }

    override suspend fun identifyWaste(image: Bitmap): UiState<Result> {
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
            val result = generateIdentification(scaledImg, confidences)

            UiState.Success(result)
        } catch (e: Exception) {
            UiState.Failure(e.message)
        }
    }

    private fun generateIdentification(
        img: Bitmap,
        confidences: FloatArray,
    ): Result {

        // Classes name from model
        val classes = arrayOf(
            "Battery", // 0
            "Biological", // 1
            "Brown Glass", // 2
            "Cardboard", // 3
            "Clothes", // 4
            "Green Glass", // 5
            "Metal", // 6
            "Paper", // 7
            "Plastic", // 8
            "Shoes", // 9
            "Trash", // 10
            "White Glass" // 11
        )

        // Classifications to match each percentage to each model classes
        val classifications = mutableListOf<Classification>()
        for (i in confidences.indices) {
            val classification = Classification(
                name = classes[i],
                percentage = confidences[i].toDouble(),
            )
            classifications.add(classification)
        }
        classifications.sortByDescending { it.percentage }

        // Generate the categoryId, typeId, and recyclable based on the highest percentage classification (first item)
        val categoryId = when(classifications[0].name) {
            "Biological" -> "c1"
            "Battery" -> "c2"
            "Metal" -> "c3"
            "Cardboard" -> "c4"
            "Clothes", "Shoes", "Trash" -> "c5"
            "Brown Glass", "Green Glass", "White Glass" -> "c6"
            "Paper" -> "c7"
            "Plastic" -> "c8"
            else -> "c1"
        }
        val typeId = when(categoryId) {
            "c1"-> "t1"
            "c3", "c6", "c8" -> "t2"
            "c2" -> "t3"
            "c4", "c7"-> "t4"
            "c5" -> "t5"
            else -> "t5"
        }
        val recyclable = when(categoryId) {
            "c1", "c5", "c2" -> false
            else -> true
        }

        return Result(
            image = img,
            classifications = classifications,
            typeId = typeId,
            categoryId = categoryId,
            recyclable = recyclable,
            createdAt = getCurrentFormattedDate()
        )
    }
}