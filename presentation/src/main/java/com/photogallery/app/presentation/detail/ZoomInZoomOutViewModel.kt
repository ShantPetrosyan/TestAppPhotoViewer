package com.photogallery.app.presentation.detail

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel

class ZoomInZoomOutViewModel: ViewModel() {

    lateinit var imageBitmap: Bitmap
    var name = ""
    var url = ""
}