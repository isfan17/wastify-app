package com.bangkit.wastify.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bangkit.wastify.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object Helper {

    // VALID EMAIL FORM VALIDATION
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
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
}