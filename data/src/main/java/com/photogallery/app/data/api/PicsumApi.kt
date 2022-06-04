package com.photogallery.app.data.api

import com.photogallery.app.data.response.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumApi {
    @GET("list")
    suspend fun getPhotos(@Query("page") pageNumber: Int, @Query("limit") limit: Int = 33): List<PhotoDto>
}