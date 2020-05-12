package online.mengchen.collectionhelper.data.file.tencent

import android.util.Log
import com.tencent.cos.xml.CosXml
import com.tencent.cos.xml.CosXmlService
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlProgressListener
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import com.tencent.cos.xml.model.PresignedUrlRequest
import com.tencent.cos.xml.model.`object`.GetObjectRequest
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider
import online.mengchen.collectionhelper.CollectHelperApplication
import online.mengchen.collectionhelper.data.file.CloudStore
import online.mengchen.collectionhelper.data.file.CloudStoreCallback
import online.mengchen.collectionhelper.data.file.CloudStoreConfiguration
import online.mengchen.collectionhelper.data.file.CloudStoreProgressListener
import online.mengchen.collectionhelper.utils.FileHelper

class TencentCOSCloudStore(override val cfg: TencentCOSConfiguration) : CloudStore {

    companion object {
        const val TAG = "TencentCOSCloudStore"
    }

    private val cosXmlService: CosXmlService
    private val transferManager: TransferManager

    init {
        val serviceConfig = CosXmlServiceConfig.Builder()
            .setRegion(cfg.region)
            .isHttps(true)
            .builder()

        val credentialsProvider =
            ShortTimeCredentialProvider(cfg.secretKeyId, cfg.secretKey, 7 * 60 * 60)
        cosXmlService =
            CosXmlService(CollectHelperApplication.context, serviceConfig, credentialsProvider)
        val transferConfig = TransferConfig.Builder()
            .setDividsionForCopy(5 * 1024 * 1024)
            .setSliceSizeForCopy(5 * 1024 * 1024)
            .setSliceSizeForUpload(1024 * 1024)
            .build()
        transferManager = TransferManager(cosXmlService, transferConfig)
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
        val cosXmlUploadTask = transferManager.upload(cfg.bucket, key, filePath, null)
        // 设置上传进度回调
        cosXmlUploadTask.setCosXmlProgressListener { complete, target ->
            val progress = complete.plus(100.0).div(target).toInt()
            Log.d(TAG, "progress = $progress")
            progressListener?.invoke(progress, complete, target)
        }

        cosXmlUploadTask.setCosXmlResultListener(object : CosXmlResultListener {
            override fun onSuccess(request: CosXmlRequest?, result: CosXmlResult?) {
                Log.d(TAG, "上传成功")
                callback?.onSuccess(key)
            }

            override fun onFail(
                request: CosXmlRequest?,
                exception: CosXmlClientException?,
                serviceException: CosXmlServiceException?
            ) {
                val message: String
                val e: Throwable = if (exception != null) {
                    message = "客户端出错"
                    exception
                } else {
                    message = "服务端出错"
                    serviceException!!
                }
                Log.d(TAG, message)
                e.printStackTrace()
                callback?.onFailed()
            }
        })
    }

    override fun downloadFile(
        bucketName: String,
        key: String,
        callback: CloudStoreCallback,
        progressListener: CloudStoreProgressListener?,
        isBigFile: Boolean
    ) {

    }

    override fun downloadFile(
        bucketName: String,
        key: String,
        filePath: String,
        callback: CloudStoreCallback?,
        progressListener: CloudStoreProgressListener?,
        isBigFile: Boolean
    ) {
        val cosXmlDownloadTask = transferManager.download(
            CollectHelperApplication.context,
            cfg.bucket,
            key,
            FileHelper.getFileDir(filePath),
            FileHelper.getFileName(filePath)
        )
        cosXmlDownloadTask.setCosXmlProgressListener { complete, target ->
            val progress = complete.plus(100.0).div(target).toInt()
            progressListener?.progressChange(progress, complete, target)
        }

        cosXmlDownloadTask.setCosXmlResultListener(object : CosXmlResultListener {
            override fun onSuccess(request: CosXmlRequest?, result: CosXmlResult?) {
                callback?.onSuccess(Unit)
            }

            override fun onFail(
                request: CosXmlRequest?,
                exception: CosXmlClientException?,
                serviceException: CosXmlServiceException?
            ) {
                Log.d(TAG, "下载失败")
                exception?.printStackTrace()
                serviceException?.printStackTrace()
                callback?.onFailed()
            }

        })
    }

    override fun getFileUrl(key: String): String? {
        val presignedUrlRequest = PresignedUrlRequest(cfg.bucket, key)
        presignedUrlRequest.setRequestMethod("GET")
        val finalUrl  = cosXmlService.getPresignedURL(presignedUrlRequest)
        Log.d(TAG, finalUrl)
        return finalUrl
    }
}