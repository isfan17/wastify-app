package com.bangkit.wastify.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.bangkit.wastify.R

class LoadingDialog(
    private val fragment: Fragment
) {
    lateinit var dialog: AlertDialog

    @SuppressLint("InflateParams")
    fun show() {
        val builder = AlertDialog.Builder(fragment.requireContext(), R.style.WrapContentDialog)
        val inflater: LayoutInflater = fragment.layoutInflater

        builder.setView(inflater.inflate(R.layout.dialog_loading, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}