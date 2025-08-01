package com.example.nfcreader.util

import android.content.Context
import android.widget.Toast

object ToastHelper {
    fun show(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}