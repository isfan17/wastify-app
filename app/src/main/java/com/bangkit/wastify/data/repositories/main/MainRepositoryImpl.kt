package com.bangkit.wastify.data.repositories.main

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import com.bangkit.wastify.data.db.WastifyDao
import com.bangkit.wastify.data.model.Identification
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.data.network.FirebaseCategory
import com.bangkit.wastify.data.network.FirebaseResult
import com.bangkit.wastify.data.network.FirebaseType
import com.bangkit.wastify.data.network.asDomainModel
import com.bangkit.wastify.ml.WastifyModel
import com.bangkit.wastify.utils.Helper
import com.bangkit.wastify.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val wastifyDao: WastifyDao,
    private val model: WastifyModel,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val dbReference: DatabaseReference
) : MainRepository {

    override suspend fun getTypes() = wastifyDao.getTypes()
    override suspend fun getCategories() = wastifyDao.getCategories()

    override suspend fun fetchTypes(): UiState<String> {
        return try {
            // Fetch data from firebase realtime db
            val dataSnapshot = dbReference.child("type").get().await()
            val networkTypes = mutableListOf<FirebaseType>()
            for (snapshot in dataSnapshot.children) {
                val networkType = snapshot.getValue(FirebaseType::class.java)
                networkType?.let {
                    networkType.id = snapshot.key
                    networkTypes.add(it)
                }
            }

            // Insert fetched data into the local Room database
            wastifyDao.clearTypes()
            wastifyDao.insertTypes(networkTypes.asDomainModel())

            UiState.Success("Types fetched successfully")
        } catch (e: Exception) {
            Timber.e(e)
            UiState.Failure(e.message)
        }
    }

    override suspend fun fetchCategories(): UiState<String> {
        return try {
            // Fetch data from firebase realtime db
            val dataSnapshot = dbReference.child("categories").get().await()
            val networkCategories = mutableListOf<FirebaseCategory>()
            for (snapshot in dataSnapshot.children) {
                val networkCategory = snapshot.getValue(FirebaseCategory::class.java)
                networkCategory?.let {
                    networkCategory.id = snapshot.key
                    networkCategories.add(it)
                }
            }

            // Insert fetched data into the local Room database
            wastifyDao.clearCategories()
            wastifyDao.insertCategories(networkCategories.asDomainModel())

            UiState.Success("Categories fetched successfully")
        } catch (e: Exception) {
            Timber.e(e)
            UiState.Failure(e.message)
        }
    }

    override fun getArticles() = wastifyDao.getArticles()
    override suspend fun getTypeById(id: String) = wastifyDao.getTypeById(id)
    override suspend fun getCategoryById(id: String) = wastifyDao.getCategoryById(id)
    override suspend fun getArticleById(id: String) = wastifyDao.getArticleById(id)

    override suspend fun getResults(): Flow<UiState<List<Result>>> = flow {
        emit(UiState.Loading)
        try {
            // Fetch data from firebase realtime db
            val dataSnapshot = firebaseAuth.currentUser?.let { dbReference.child("results").child(it.uid).get().await() }
            val networkResults = mutableListOf<FirebaseResult>()
            if (dataSnapshot != null) {
                for (snapshot in dataSnapshot.children) {
                    val networkResult = snapshot.getValue(FirebaseResult::class.java)
                    networkResult?.let { networkResults.add(it) }
                }
            }

            // Insert fetched data into the local Room database
            wastifyDao.clearResults()
            wastifyDao.insertResults(networkResults.asDomainModel())

            // Emit the fetched data as a Success
            emit(UiState.Success(networkResults.asDomainModel()))
        } catch (e: Exception) { Timber.e(e) }

        val localData = wastifyDao.getResults().map { UiState.Success(it) }
        emitAll(localData)
    }.catch {e ->
        emit(UiState.Failure(e.message))
    }

    override suspend fun storeResult(identification: Identification): UiState<String> {
        return try {
            firebaseAuth.currentUser?.let {  firebaseUser ->
                // Store bitmap in storage
                val storageRef = firebaseStorage.reference.child("results_img/${firebaseUser.uid}/${System.currentTimeMillis()}")
                val outputStream = ByteArrayOutputStream()
                identification.image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val compressedImg = outputStream.toByteArray()

                val objResult = FirebaseResult(
                    name = identification.result,
                    percentage = identification.percentage,
                    typeId = identification.typeId,
                    categoryId = identification.categoryId,
                    recyclable = identification.recyclable,
                    createdAt = Helper.getCurrentFormattedDate()
                )

                val uploadTask = storageRef.putBytes(compressedImg)
                val uploadResult = uploadTask.await()

                if (uploadResult.task.isSuccessful) {
                    val downloadUrl = storageRef.downloadUrl.await()
                    objResult.imageUrl = downloadUrl.toString()
                } else {
                    Timber.d("Failed to store image")
                }

                val resultRef = dbReference.child("results")
                val resultId = resultRef.child(firebaseUser.uid).push().key
                if (resultId != null) { resultRef.child(firebaseUser.uid).child(resultId).setValue(objResult) }
            }
            UiState.Success("Result saved successfully")
        } catch (e: Exception) {
            Timber.e(e)
            UiState.Failure(e.message)
        }
    }

    override suspend fun classifyWaste(image: Bitmap): UiState<Identification> {
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
    ): Identification {
        // Result
        val classes = arrayOf("battery", "biological", "brown-glass", "cardboard", "clothes", "green-glass", "metal", "paper", "plastic", "shoes", "trash", "white-glass")
        val result = classes[maxPos]

        val categoryId = when(maxPos) {
            0 -> "c2"
            1 -> "c1"
            2, 5, 11 -> "c6"
            3 -> "c4"
            4 -> "c5"
            6 -> "c3"
            7 -> "c7"
            8 -> "c8"
            9 -> "c5"
            10 -> "c5"
            else -> "c2"
        }
        val typeId = when(categoryId) {
            "c2" -> "t3"
            "c3", "c6", "c8" -> "t2"
            "c4", "c7"-> "t4"
            "c1"-> "t1"
            "c5" -> "t5"
            else -> "5"
        }
        val recyclable = when(categoryId) {
            "c1", "c5", "c2" -> false
            else -> true
        }

        return Identification(
            img, result, maxConfidence, typeId, categoryId, recyclable
        )
    }
}