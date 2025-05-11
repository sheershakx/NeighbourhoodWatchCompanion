package com.srg.neighbourhoodwatchcompanion.common


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.PickerMediaColumns.DISPLAY_NAME
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlin.math.roundToInt

class ImageCompressor(
    private val context: Context
) {
    suspend fun compressImage(
        contentUri: Uri,
        compressionThreshold: Long
    ): ByteArray? {
        return withContext(Dispatchers.IO) {
            val mimeType = context.contentResolver.getType(contentUri)
            val inputBytes = context
                .contentResolver
                .openInputStream(contentUri)
                ?.use { inputStream ->
                    inputStream.readBytes()
                } ?: return@withContext null

            ensureActive()

            withContext(Dispatchers.Default) {
                val bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes.size)

                ensureActive()

                val compressFormat = when (mimeType) {
                    "image/png" -> Bitmap.CompressFormat.PNG
                    "image/jpeg" -> Bitmap.CompressFormat.JPEG
                    "image/webp" -> if (Build.VERSION.SDK_INT >= 30) {
                        Bitmap.CompressFormat.WEBP_LOSSLESS
                    } else Bitmap.CompressFormat.WEBP

                    else -> Bitmap.CompressFormat.JPEG
                }

                var outputBytes: ByteArray
                var quality = 90

                do {
                    ByteArrayOutputStream().use { outputStream ->
                        bitmap.compress(compressFormat, quality, outputStream)
                        outputBytes = outputStream.toByteArray()
                        quality -= (quality * 0.1).roundToInt()
                    }
                } while (isActive &&
                    outputBytes.size > compressionThreshold &&
                    quality > 5 &&
                    compressFormat != Bitmap.CompressFormat.PNG
                )

                outputBytes
            }
        }
    }


    suspend fun getUriFilenameWithExtension(uri: Uri) =
        withContext(Dispatchers.IO) {
            // Fetch filename from DocumentProvider/FileProvider
            context.contentResolver.query(uri, arrayOf(DISPLAY_NAME), null, null, null)
                ?.use { cursor ->
                    val nameIndex = cursor.getColumnIndexOrThrow(DISPLAY_NAME)
                    if (cursor.moveToFirst()) {
                        val filename = UUID.randomUUID().toString()+cursor.getString(nameIndex)

                        if (!filename.contains(".")) {
                            // Retrieve the MIME type using the ContentResolver
                            val mimeType = context.contentResolver.getType(uri)

                            // Get the file extension, if possible
                            val extension =
                                MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                                    ?: return@withContext null

                            return@withContext "$filename.$extension"
                        } else {
                            return@withContext filename
                        }
                    }
                }

            return@withContext null
        }
}