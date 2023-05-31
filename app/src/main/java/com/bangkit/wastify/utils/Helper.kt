package com.bangkit.wastify.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

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
}