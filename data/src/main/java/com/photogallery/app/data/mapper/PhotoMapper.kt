package com.photogallery.app.data.mapper

import com.photogallery.app.data.Mapper
import com.photogallery.app.data.response.dto.PhotoDto
import com.photogallery.app.domain.models.PicsumPhoto

class PhotoMapper: Mapper<PhotoDto, PicsumPhoto> {
    override fun map(input: PhotoDto): PicsumPhoto =
        PicsumPhoto(input.id,
                    input.author,
                    input.width,
                    input.height,
                    input.url,
                    input.downloadUrl)
}