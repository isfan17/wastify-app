package com.bangkit.wastify.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.bangkit.wastify.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.libraries.places.api.model.LocalTime
import com.google.android.libraries.places.api.model.OpeningHours
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import com.bangkit.wastify.data.model.Result

object Helper {

    // VALID EMAIL FORM VALIDATION
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }

    // GET CURRENT FORMATTED DATE
    fun getCurrentFormattedDate(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
        return formatter.format(currentDate)
    }

    // TOAST FUNCTION SIMPLIFIED FOR FRAGMENT
    fun Fragment.toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    // CREATE PHOTO FILE
    private const val FILENAME_FORMAT = "dd-MMM-yyyy"
    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())
    fun createPhotoFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory =
            if (mediaDir != null && mediaDir.exists()) mediaDir
            else application.filesDir

        return File(outputDirectory, "Wastify-$timeStamp.jpg")
    }

    // ROTATE BITMAP TO FIX AUTO ROTATE BUG FROM CAMERAX
    fun rotateBitmap(bitmap: Bitmap, isBackCamera: Int): Bitmap {
        val matrix = Matrix()

        return when (isBackCamera) {
            // Image from Gallery
            0 -> bitmap
            // Image from Back Camera
            1 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) matrix.postRotate(90f)
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
            // Image from Front Camera
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) matrix.postRotate(-90f)
                matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f) // flips image
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
        }
    }

    // CONVERT URI TO FILE FORMAT
    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    // TEMP STORAGE FOR FILE
    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    // CONVERT URI TO BITMAP
    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // CONVERT VECTOR TO BITMAP DESCRIPTOR
    fun vectorToBitmap(@DrawableRes id: Int, res: Resources): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(res, id, null)
        if (vectorDrawable == null) {
            Timber.e("Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    // FORMAT OPENING HOURS OBJECT TO STRING
    fun formatOpeningHours(openingHours: OpeningHours?): String {
        openingHours?.periods?.firstOrNull()?.let { period ->
            val openTime = formatTime(period.open?.time)
            val closeTime = formatTime(period.close?.time)

            if (openTime != null && closeTime != null) {
                return "$openTime - $closeTime"
            }
        }
        return "Unknown"
    }

    // FORMAT TIME FOR OPENING HOURS
    private fun formatTime(time: LocalTime?): String? {
        return time?.let {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, time.hours)
            calendar.set(Calendar.MINUTE, time.minutes)

            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            format.format(calendar.time)
        }
    }

    fun hashMapToListOfString(hashMap: HashMap<String, String>): List<String> {
        return hashMap.map { it.value }
    }

    fun formatListToString(strings: List<String>): String {
        val stringBuilder = StringBuilder()

        for (string in strings) {
            stringBuilder.append("- $string\n")
        }

        return stringBuilder.toString().trimEnd()
    }
    fun countFoundCategories(identifications: List<Result>): String {
        val categoryIdentificationStatus = HashMap<String, Boolean>()

        // Iterate over the identifications and update the identification status
        for (identification in identifications) {
            val categoryId = identification.categoryId
            categoryIdentificationStatus[categoryId] = true
        }

        // Count the number of found categories
        var foundCategoriesCount = 0
        for (status in categoryIdentificationStatus.values) {
            if (status) {
                foundCategoriesCount++
            }
        }

        return foundCategoriesCount.toString()
    }


}