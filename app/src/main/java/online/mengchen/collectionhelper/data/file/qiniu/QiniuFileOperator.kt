package online.mengchen.collectionhelper.data.file.qiniu

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import online.mengchen.collectionhelper.data.file.FileOperator
import online.mengchen.collectionhelper.data.file.FileUploadListener
import java.io.File
import java.io.InputStream

class QiniuFileOperator(val token: String): FileOperator {

    companion object {
        const val TAG = "QiniuFileOperator"
    }

    var mFileUploadListener: FileUploadListener? = null

    override fun upload(key: String, file: File): Boolean {
        var res = false
        QiniuConfig.uploadManager.put(file, key, token, { key, info, response ->
            res = if (info.isOK) {
                Log.d(TAG, "文件名为 $key 的文件上传成功")
                true
            } else {
                Log.e(TAG, "文件名为 $key 的文件上传失败")
                false
            }
            Log.d("TAG", "key = $key info = $info response =  $response")
        }, null)
        return res
    }

    fun upload(key: String, bytes: ByteArray): Boolean {
        var res = false
        QiniuConfig.uploadManager.put(bytes, key, token, { key, info, response ->
            res = if (info.isOK) {
                mFileUploadListener?.uploadSuccess()
                Log.d(TAG, "文件名为 $key 的文件上传成功")
                true
            } else {
                mFileUploadListener?.uploadFailuare()
                Log.e(TAG, "文件名为 $key 的文件上传失败")
                false
            }
            Log.d("TAG", "key = $key info = $info response =  $response")
        }, null)
        return res
    }


    override fun delete(id: String) {

    }

    override fun getFile(id: String) {
    }

}