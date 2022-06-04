package com.photogallery.app.presentation.helper

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

@Suppress("DEPRECATION")
internal fun saveImageToStorage(
    context: Context,
    bitmap: Bitmap,
    filename: String = "screenshot.jpg",
    mimeType: String =  "image/jpeg",
    directory: String = Environment.DIRECTORY_PICTURES,
    mediaContentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
): Boolean {
    val imageOutStream: OutputStream
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                put(MediaStore.Images.Media.RELATIVE_PATH, directory)
            }

            context.contentResolver.run {
                val uri =
                    context.contentResolver.insert(mediaContentUri, values)
                        ?: return false
                imageOutStream = openOutputStream(uri) ?: return false
            }
        } else {
            val imagePath = Environment.getExternalStoragePublicDirectory(directory).absolutePath
            val image = File(imagePath, filename)
            imageOutStream = FileOutputStream(image)
        }

        imageOutStream.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }

        return true
    } catch (e: Exception) {
        return false
    }
}