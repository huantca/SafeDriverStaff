package com.bkplus.callscreen.ultis

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File

fun ImageView.loadImage(uri: Uri?) {
    if (uri == null) return
    Glide.with(this).load(uri).into(this)
}

fun ImageView.loadImage(string: String?) {
    Glide.with(this).load(string).into(this)
}

fun ImageView.loadImage(file: File) {
    Glide.with(this).load(file).into(this)
}
