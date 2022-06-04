package com.photogallery.app.data

import com.photogallery.app.data.api.PicsumApi
import com.photogallery.app.data.response.dto.PhotoDto
import com.photogallery.app.domain.models.PicsumPhoto
import com.photogallery.app.domain.repositories.PicsumPhotosRepository

class PicsumPhotosRepositoryImpl(
    private val picsumApi: PicsumApi,
    private val photoMapper: Mapper<PhotoDto, PicsumPhoto>,
): PicsumPhotosRepository {

    override suspend fun getPhotos(pageNumber: Int): List<PicsumPhoto> {
        return picsumApi.getPhotos(pageNumber).map { photoDto ->
            photoMapper.map(photoDto)
        }
    }
}