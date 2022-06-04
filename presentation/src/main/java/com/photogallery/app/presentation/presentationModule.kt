package com.photogallery.app.presentation

import com.photogallery.app.presentation.detail.ZoomInZoomOutViewModel
import com.photogallery.app.presentation.mainpage.GalleryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { GalleryViewModel(get()) }
    viewModel { ZoomInZoomOutViewModel() }
}
