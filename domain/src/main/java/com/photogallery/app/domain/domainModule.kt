package com.photogallery.app.domain

import com.photogallery.app.domain.usecases.*
import org.koin.dsl.module

val domainModule = module {
    single<GetPicsumPhotosUseCase> { GetPicsumPhotosUseCaseImpl(get()) }
}