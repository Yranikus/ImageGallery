package com.example.myapplication3333.srvices

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore

class UriService {

    companion object {
        fun getContentUriId(imageUri: Uri, context: Context): Uri? {
            val projections = arrayOf(MediaStore.MediaColumns._ID)
            val cursor: Cursor? = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projections,
                MediaStore.MediaColumns.DATA + "=?", arrayOf(imageUri.path), null
            )
            var id: Long = 0
            if (cursor != null) {
                if (cursor.count > 0) {
                    cursor.moveToFirst()
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                }
            }
            cursor?.close()
            return Uri.withAppendedPath(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id.toInt().toString()
            )
        }
    }


}