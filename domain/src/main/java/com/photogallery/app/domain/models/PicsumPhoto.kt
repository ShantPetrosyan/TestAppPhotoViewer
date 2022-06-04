package com.photogallery.app.domain.models

data class PicsumPhoto(val id: String = "",
                       val author: String = "",
                       val width: Int = 0,
                       val height: Int = 0,
                       val url: String = "",
                       val downloadUrl: String = "")