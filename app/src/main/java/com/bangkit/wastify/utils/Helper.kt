package com.bangkit.wastify.utils

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.util.Base64
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

object Helper {

    // VALID EMAIL FORM VALIDATION
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }

    // GET CURRENT FORMATTED DATE
    fun getCurrentFormattedDate(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
        return formatter.format(currentDate)
    }

    // CONVERT DATE IN MILLIS TO STRING
    fun convertDateMillisToString(timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
        return formatter.format(calendar.time)
    }

    // CONVERT DATE IN STRING TO MILLIS
    fun convertDateStringToMillis(dateString: String): Long {
        val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val date = sdf.parse(dateString)
        return date?.time ?: 0L
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

    // CONVERT DOUBLE TO ROUND UP 2 DIGIT PERCENTAGE IN STRING FORMAT
    fun doubleToPercentageString(value: Double): String {
        // Convert the double value to a percentage with two decimal places
        val percentage = value * 100

        // Round up the percentage to the nearest integer
        val roundedPercentage = ceil(percentage).toInt()

        // Convert the rounded percentage to a string
        return "$roundedPercentage%"
    }

    fun byteArrayStringToBitmap(bytesString: String): Bitmap {
        val byteArray = Base64.decode(bytesString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun bitmapToByteArrayString(bmp: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }
}