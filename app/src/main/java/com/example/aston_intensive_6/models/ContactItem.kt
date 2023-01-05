package com.example.aston_intensive_6.models

import android.graphics.Bitmap

data class ContactItem(
    var id: Int,
    val firstName: String,
    val secondName: String,
    val telNumber: String,
    var picture: Bitmap?
)
