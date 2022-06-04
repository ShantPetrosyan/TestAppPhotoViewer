package com.photogallery.app.domain.repositories

import com.photogallery.app.domain.models.PicsumPhoto

interface PicsumPhotosRepository {
    suspend fun getPhotos(pageNumber: Int): List<PicsumPhoto>
}