package com.photogallery.app.data

import com.photogallery.app.data.api.PicsumApi
import com.photogallery.app.data.mapper.PhotoMapper
import com.photogallery.app.data.network.createOkHttpClient
import com.photogallery.app.data.network.createRetrofit
import com.photogallery.app.data.response.dto.PhotoDto
import com.photogallery.app.domain.models.PicsumPhoto
import com.photogallery.app.domain.repositories.PicsumPhotosRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single { createOkHttpClient() }

    single<PicsumApi> { createRetrofit(get(), BuildConfig.API_URL) }

    single<Mapper<PhotoDto, PicsumPhoto>>(named("picsum_map")) { PhotoMapper() }

    single<PicsumPhotosRepository> { PicsumPhotosRepositoryImpl(get(), get(named("picsum_map"))) }
}