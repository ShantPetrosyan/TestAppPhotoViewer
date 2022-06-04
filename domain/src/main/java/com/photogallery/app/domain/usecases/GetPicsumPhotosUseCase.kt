package com.photogallery.app.domain.usecases

import com.photogallery.app.domain.models.PicsumPhoto
import com.photogallery.app.domain.repositories.PicsumPhotosRepository

interface GetPicsumPhotosUseCase {
    suspend operator fun invoke(pageNumber: Int): List<PicsumPhoto>
}

class GetPicsumPhotosUseCaseImpl(private val picsumPhotosRepository: PicsumPhotosRepository): GetPicsumPhotosUseCase {
    override suspend fun invoke(pageNumber: Int): List<PicsumPhoto> = picsumPhotosRepository.getPhotos(pageNumber)
}