package com.photogallery.app.data

import com.photogallery.app.data.mapper.PhotoMapper
import com.photogallery.app.data.response.dto.PhotoDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PhotoMapperTest {

    private lateinit var mapper: PhotoMapper
    private lateinit var photoDto: PhotoDto

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mapper = PhotoMapper()
        photoDto = PhotoDto("my_id", "author", 1, 2, "url", "download_url")
    }

    @Test
    fun `check that mapping is correct success case`() {
        val result = mapper.map(photoDto)

        assertEquals(result.author, photoDto.author)
        assertEquals(result.downloadUrl, photoDto.downloadUrl)
        assertEquals(result.url, photoDto.url)
        assertEquals(result.id, photoDto.id)
        assertEquals(result.width, photoDto.width)
        assertEquals(result.height, photoDto.height)
    }
}