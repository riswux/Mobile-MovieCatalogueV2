package com.example.moviecatalog

import android.app.Dialog
import android.content.Context

class ProgressDialog(context: Context): Dialog(context) {
    init {
        setContentView(R.layout.loading_layout)
    }
}