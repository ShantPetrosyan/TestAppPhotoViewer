package com.photogallery.app.presentation.mainpage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.photogallery.app.domain.models.PicsumPhoto
import com.photogallery.app.presentation.R
import com.photogallery.app.presentation.databinding.ActivityGalleryBinding
import com.photogallery.app.presentation.detail.ZoomInZoomOutActivity
import com.photogallery.app.presentation.helper.NetworkHandler.isOnline
import com.photogallery.app.presentation.mainpage.extensions.invisible
import com.photogallery.app.presentation.mainpage.extensions.visible
import com.photogallery.app.presentation.helper.PaginationScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryActivity : AppCompatActivity() {

    private val viewModel by viewModel<GalleryViewModel>()
    private var isDataLoading = false
    private lateinit var binding: ActivityGalleryBinding

    private val adapter by lazy {
        GalleryAdapter { photo, view ->
            viewModel.photoClicked(photo, view)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar.toolbar)

        binding.swipeContainer.setOnRefreshListener {
            loadMoviesList()
        }

        setObserving()
        initializeView()
        loadMoviesList()
    }

    private fun setObserving() {
        viewModel.state.observe(this) { state ->
            adapter.removeLoadingFooter()
            binding.swipeContainer.isRefreshing = false
            isDataLoading = false
            binding.mainBody.progressbar.visibility = View.GONE
            hideProgress()

            when (state) {
                is GalleryViewModel.MainState.Error -> renderFailure(state.message)
                is GalleryViewModel.MainState.PhotoList -> displayGalleryPhotos(state.photos)
                is GalleryViewModel.MainState.NavigateToDetail -> {
                    startActivity(ZoomInZoomOutActivity.getIntent(this, state.photo.downloadUrl, state.photo.author))
                }
            }
        }
    }

    private fun initializeView() {
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.mainBody.rvPhotoGallery.layoutManager = layoutManager
        binding.mainBody.rvPhotoGallery.adapter = adapter

        binding.mainBody.rvPhotoGallery.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                isDataLoading = true
                viewModel.pageNumber += 1
                loadMoviesList()
            }

            override val isLastPage: Boolean
                get() = false

            override val isLoading: Boolean
                get() = isDataLoading
        })
    }

    private fun renderFailure(message: String) {
        binding.mainBody.rvPhotoGallery.invisible()
        binding.mainBody.emptyView.visible()
        notifyWithAction(message, ::loadMoviesList)
    }

    private fun loadMoviesList() {
        binding.mainBody.emptyView.invisible()
        binding.mainBody.rvPhotoGallery.visible()
        if (isOnline(this)) {
            showProgress()
            viewModel.loadGallery()
        } else {
            hideProgress()
            binding.mainBody.rvPhotoGallery.invisible()
            binding.mainBody.emptyView.visible()
            Toast.makeText(this, getString(R.string.device_offline), Toast.LENGTH_LONG).show()
        }
    }

    private fun displayGalleryPhotos(photos: List<PicsumPhoto>) {
        hideProgress()
        adapter.collection.addAll(photos)
        adapter.addLoadingFooter()
    }

    private fun showProgress() = progressStatus(View.VISIBLE)

    private fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) =
        with(this) { this.binding.mainToolbar.progress.visibility = viewStatus }

    private fun notifyWithAction(
        message: String,
        action: () -> Any
    ) {
        val snackBar = Snackbar.make(binding.mainBody.emptyView, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(R.string.action_refresh) { _ -> action.invoke() }
        snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.colorTextPrimary))
        snackBar.show()
    }
}