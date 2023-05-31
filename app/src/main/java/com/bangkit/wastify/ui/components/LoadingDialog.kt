package com.bangkit.wastify.ui.components

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import com.bangkit.wastify.R

class LoadingDialog(private val activity: Activity) {
    lateinit var dialog: AlertDialog

    fun show() {
        val builder = AlertDialog.Builder(activity, R.style.WrapContentDialog)
        val inflater: LayoutInflater = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.dialog_loading, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}