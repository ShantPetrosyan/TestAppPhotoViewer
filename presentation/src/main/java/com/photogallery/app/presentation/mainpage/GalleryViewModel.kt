package com.photogallery.app.presentation.mainpage

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photogallery.app.domain.models.PicsumPhoto
import com.photogallery.app.domain.usecases.GetPicsumPhotosUseCase
import com.photogallery.app.presentation.helper.NetworkHandler.isOnline
import kotlinx.coroutines.launch

class GalleryViewModel(private val getPhotos: GetPicsumPhotosUseCase): ViewModel() {
    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state
    var pageNumber = 1

    fun loadGallery() {
        viewModelScope.launch {
            kotlin.runCatching { getPhotos(pageNumber) }
                .onSuccess {
                    _state.value = MainState.PhotoList(it)
                }
                .onFailure {
                    _state.value = it.message?.let { it1 -> MainState.Error(it1) }
                }
        }
    }

    fun photoClicked(photo: PicsumPhoto, view: View) {
        _state.value = MainState.NavigateToDetail(photo, view)
    }

    sealed class MainState {
        data class Error(val message: String): MainState()
        data class PhotoList(val photos: List<PicsumPhoto>): MainState()
        data class NavigateToDetail(val  photo: PicsumPhoto, val view: View): MainState()
    }
}