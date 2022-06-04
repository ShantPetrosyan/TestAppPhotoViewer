package com.photogallery.app.presentation.detail

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.photogallery.app.presentation.R
import com.photogallery.app.presentation.databinding.ActivityZoomInZoomOutBinding
import com.photogallery.app.presentation.helper.saveImageToStorage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream

class ZoomInZoomOutActivity : AppCompatActivity() {
    private val viewModel by viewModel<ZoomInZoomOutViewModel>()

    private lateinit var binding: ActivityZoomInZoomOutBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityZoomInZoomOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getExtraValues()
    }

    private fun getExtraValues() {
        intent?.extras?.let {
            if (it.containsKey(ARG_IMAGE_URL) && it.containsKey(ARG_IMAGE_NAME)) {
                viewModel.url = it.getString(ARG_IMAGE_URL, "")
                viewModel.name = it.getString(ARG_IMAGE_NAME, "")
                loadImage(viewModel.url)
            }
        }
    }

    private fun loadImage(url: String?) {
        url?.let {
            Glide.with(this).asBitmap().load(url)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                    ) {
                        viewModel.imageBitmap = resource
                        binding.ivZoomable.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                checkSavePermissions()
                true
            }
            R.id.action_share -> {
                showShareIntent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkSavePermissions() {
        if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_CODE_WRITE_STORAGE)
        } else {
            saveImage()
        }
    }

    private fun showStatusMessage(saved: Boolean) {
        Toast.makeText(this, getString(if (saved) R.string.saved else R.string.failed_to_save), Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            REQUEST_CODE_WRITE_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    saveImage()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun saveImage() {
        val isSaved = saveImageToStorage(this, viewModel.imageBitmap, "${viewModel.name}.jpg")
        showStatusMessage(isSaved)
    }

    private fun showShareIntent() {
        // Step 1: Create Share itent
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")

        // Step 2: Get Bitmap from your imageView
        val bitmap = binding.ivZoomable.drawable.toBitmap() // your imageView here.

        // Step 3: Compress image
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        // Step 4: Save image & get path of it
        val path = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, "tempimage", null)

        // Step 5: Get URI of saved image
        val uri = Uri.parse(path)

        // Step 6: Put Uri as extra to share intent
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // Step 7: Start/Launch Share intent
        startActivity(intent)
    }

    companion object {
        private const val ARG_IMAGE_URL = "ARG_IMAGE_URL"
        private const val ARG_IMAGE_NAME = "ARG_IMAGE_NAME"
        private const val REQUEST_CODE_WRITE_STORAGE = 2

        fun getIntent(context: Context, url: String, authorName: String):
                Intent = Intent(context, ZoomInZoomOutActivity::class.java).apply {
            putExtra(ARG_IMAGE_URL, url)
            putExtra(ARG_IMAGE_NAME, authorName)
        }
    }
}