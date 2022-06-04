package com.photogallery.app.presentation.mainpage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.photogallery.app.domain.models.PicsumPhoto
import com.photogallery.app.presentation.R
import com.photogallery.app.presentation.databinding.RowPhotoBinding
import com.photogallery.app.presentation.databinding.RowProgressBinding
import com.photogallery.app.presentation.mainpage.extensions.loadFromUrl
import kotlin.properties.Delegates

@SuppressLint("NotifyDataSetChanged")
class GalleryAdapter(private val onClick: (PicsumPhoto, View) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoadingAdded = false

    internal var collection: ArrayList<PicsumPhoto> by Delegates.observable(ArrayList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            Types.ITEM.ordinal -> {
                PhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_photo, parent, false))
            }
            Types.PROGRESS.ordinal -> {
                ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_progress, parent, false))
            } else -> {
                throw Exception("Unknown type = $viewType")
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = collection[viewHolder.adapterPosition]

        when (getItemViewType(position)) {
            Types.ITEM.ordinal -> {
                (viewHolder as PhotoViewHolder).bind(item)
            }
            Types.PROGRESS.ordinal -> {
                (viewHolder as ProgressViewHolder).bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == collection.size - 1 && isLoadingAdded)
            Types.PROGRESS.ordinal
        else Types.ITEM.ordinal
    }

    override fun getItemCount() = collection.size

    fun addLoadingFooter() {
        isLoadingAdded = true
        addItem(PicsumPhoto())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = itemCount - 1
        if (position >= 0) {
            collection.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun addItem(item: PicsumPhoto) {
        collection.add(item)
        notifyItemInserted(itemCount - 1)
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RowPhotoBinding.bind(itemView)

        fun bind(picsumPhoto: PicsumPhoto) {
            with (itemView) {
                binding.photoPoster.loadFromUrl(picsumPhoto.downloadUrl)
                binding.txtAuthorTitle.text = picsumPhoto.author

                ViewCompat.setTransitionName(binding.photoPoster, picsumPhoto.author)
                setOnClickListener {
                    onClick(picsumPhoto, binding.photoPoster)
                }
            }
        }
    }

    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RowProgressBinding.bind(itemView)

        fun bind() {
            binding.loadingProgress.visibility = View.VISIBLE
        }
    }

    enum class Types {
        ITEM,
        PROGRESS
    }
}

