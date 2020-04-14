package online.mengchen.collectionhelper.utils

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.util.*

/**
 *
 */
object UriHelper {
    fun getImagePath(uri: Uri, context: Context): String? {
        var path: String? = null
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是 document 类型的 Uri，则通过 Document id 处理
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri.authority) {
                val id = docId.split(":")[1] // 解析出数字格式的 id
                val selection = MediaStore.Images.Media._ID + "=" + id
                path = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads//public_downloads"),
                    docId.toLong()
                )
                path = getImagePath(context, contentUri, null)
            }
        } else if ("content" == uri.scheme?.toLowerCase(Locale.ROOT)) {
            // 如果是 content 类型的 Uri，则使用普通方式处理
            path = getImagePath(context, uri, null)
        } else if ("file" == uri.scheme?.toLowerCase(Locale.ROOT)) {
            // 如果是 file 类型，直接获取图片路径
            path = uri.path
        }
        return path
    }

    private fun getImagePath(context: Context, uri: Uri, selection: String?): String? {
        var path: String? = null
        val cursor = context.contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
        }
        return path
    }
}