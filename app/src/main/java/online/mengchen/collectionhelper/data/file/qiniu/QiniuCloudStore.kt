package online.mengchen.collectionhelper.data.file.qiniu

import android.app.DownloadManager
import android.util.Base64
import android.util.Log
import com.google.common.io.BaseEncoding
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.qiniu.android.storage.*
import com.qiniu.util.Auth
import online.mengchen.collectionhelper.common.Constant
import online.mengchen.collectionhelper.common.FileType
import online.mengchen.collectionhelper.common.FileType.TypeFile
import online.mengchen.collectionhelper.data.file.CloudStore
import online.mengchen.collectionhelper.data.file.CloudStoreCallback
import online.mengchen.collectionhelper.data.file.CloudStoreObject
import online.mengchen.collectionhelper.data.file.CloudStoreProgressListener
import org.json.JSONObject
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class QiniuCloudStore(override val cfg: QiniuConfiguration) : CloudStore {

    companion object {
        const val TAG = "QiniuCloudStore"
        private const val HMAC_SHA1_ALGORITHM = "HmacSHA1"
    }

    private val config = Configuration.Builder()
        .connectTimeout(10)
        .useHttps(true)
        .responseTimeout(60)
        .build()
    private val uploadManager = UploadManager(config)

    private val token: String
    private val imageToken: String
    private val documentToken: String
    private val musicToken: String
    private val videoToken: String

    init {
        val auth = Auth.create(cfg.accessKey, cfg.secretKey)
        token = auth.uploadToken(cfg.bucket)
        imageToken = auth.uploadToken(cfg.imageBucket ?: cfg.bucket)
        documentToken = auth.uploadToken(cfg.documentBucket ?: cfg.bucket)
        musicToken = auth.uploadToken(cfg.musicBucket ?: cfg.bucket)
        videoToken = auth.uploadToken(cfg.videoBucket ?: cfg.bucket)
    }

    override fun uploadFile(
        bucketName: String,
        key: String,
        filePath: String,
        fileType: Int,
        progressListener: ((Int, Long, Long) -> Unit)?,
        callback: CloudStoreCallback?,
        isBigFile: Boolean
    ) {
        val finalKey = getPath(fileType) + key
        uploadManager.put(
            filePath, finalKey, getToken(fileType),
            { key, info, _ ->
                if (info.isOK) {
                    Log.d(TAG, "文件上传成功: $key")
                    callback?.onSuccess(Unit)
                } else {
                    Log.d(TAG, "文件上传失败: $key")
                    callback?.onFailed()
                }
            }, UploadOptions(
                null, null, false,
                UpProgressHandler { key, percent ->
                    progressListener?.invoke(
                        percent.toInt(),
                        -1,
                        -1
                    )
                }, null
            )
        )
    }

    override fun downloadFile(
        bucketName: String,
        key: String,
        filePath: String,
        callback: CloudStoreCallback?,
        progressListener: CloudStoreProgressListener?,
        isBigFile: Boolean
    ) {
        val url = getFileUrl(key)
        FileDownloader.getImpl().create(url)
            .setListener(object : FileDownloadListener() {
                override fun warn(task: BaseDownloadTask?) {

                }

                override fun completed(task: BaseDownloadTask?) {
                    callback?.onSuccess(CloudStoreObject())
                    Log.d(TAG, "下载完成 taskId = ${task?.id}")
                }

                override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                }

                override fun error(task: BaseDownloadTask?, e: Throwable?) {
                    callback?.onFailed()
                    Log.d(TAG, "下载失败 taskId = ${task?.id}")
                }

                override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    val progress: Int = soFarBytes.plus(100.0).div(totalBytes).toInt()
                    progressListener?.progressChange(progress, soFarBytes.toLong(), totalBytes.toLong())
                }

                override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                }

            }).start()
    }

    override fun downloadFile(
        bucketName: String,
        key: String,
        callback: CloudStoreCallback,
        progressListener: CloudStoreProgressListener?,
        isBigFile: Boolean
    ) {
        val url = getFileUrl(key)

    }

    override fun getFileUrl(key: String): String? {
        val domain = cfg.domain
        val fileTimeOut: Long = System.currentTimeMillis().plus(Constant.FILE_TIME_OUT)
        val fileUrl = "$domain/$key?e=$fileTimeOut"
        val sign = signUseHMACSHA1(cfg.secretKey, fileUrl)
        val token = "${cfg.accessKey}.$sign"
        return "$fileUrl&token=$token"
    }

    private fun getToken(@TypeFile type: Int): String {
        return when (type) {
            FileType.DOCUMENT -> {
                documentToken
            }
            FileType.IMAGE -> {
                imageToken
            }
            FileType.VIDEO -> {
                videoToken
            }
            FileType.MUSIC -> {
                musicToken
            }
            else -> {
                token
            }
        }
    }

    private fun signUseHMACSHA1(key: String, data: String): String {
        val byteKey = key.toByteArray()
        val signKey = SecretKeySpec(byteKey, HMAC_SHA1_ALGORITHM)
        val mac = Mac.getInstance(HMAC_SHA1_ALGORITHM)
        mac.init(signKey)
        val rawMac = mac.doFinal(data.toByteArray())
        return urlSafeBase64(rawMac)
    }

    private fun urlSafeBase64(data: ByteArray): String {
        return BaseEncoding.base64Url().encode(data)
    }

    private fun getPath(@TypeFile type: Int): String {
        return when (type) {
            FileType.DOCUMENT -> {
                cfg.documentPath ?: "document/"
            }
            FileType.IMAGE -> {
                cfg.imagePath ?: "image/"
            }
            FileType.VIDEO -> {
                cfg.videoPath ?: "video/"
            }
            FileType.MUSIC -> {
                cfg.musicPath ?: "music/"
            }
            else -> {
                ""
            }
        }
    }
}