package online.mengchen.collectionhelper.data.file.aliyun

import android.util.Log
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider
import com.alibaba.sdk.android.oss.model.*
import online.mengchen.collectionhelper.CollectHelperApplication
import online.mengchen.collectionhelper.data.file.CloudStore
import online.mengchen.collectionhelper.data.file.CloudStoreCallback
import online.mengchen.collectionhelper.data.file.CloudStoreObject
import online.mengchen.collectionhelper.data.file.CloudStoreProgressListener

class AliyunCloudStore(
    override val cfg: AliyunConfiguration,
    onSuccess: ((String) -> Unit)?,
    onFailure: ((String, Throwable) -> Unit)?
) : CloudStore {

    companion object {
        const val TAG = "AliyunCloudStore"
    }

    private var ossClient: OSSClient

    private val endpoint = "http://oss-cn-beijing.aliyuncs.com"
    private val endpointTemplate = "http://%s.aliyuncs.com"

    init {
        val credentialsProvider =
            OSSPlainTextAKSKCredentialProvider(cfg.accessKey, cfg.secretKey)
        ossClient = OSSClient(CollectHelperApplication.context, endpoint, credentialsProvider)
        val request = GetBucketInfoRequest(cfg.bucket)
        ossClient.asyncGetBucketInfo(
            request,
            object : OSSCompletedCallback<GetBucketInfoRequest, GetBucketInfoResult> {
                override fun onSuccess(
                    request: GetBucketInfoRequest?,
                    result: GetBucketInfoResult?
                ) {
                    val location = result?.bucket?.location
                    val endpoint = endpointTemplate.format(location)
                    ossClient =
                        OSSClient(CollectHelperApplication.context, endpoint, credentialsProvider)
                    onSuccess?.let { it("初始化成功") }
                }

                override fun onFailure(
                    request: GetBucketInfoRequest?,
                    clientException: ClientException?,
                    serviceException: ServiceException?
                ) {
                    val exception: Throwable
                    val status: String
                    if (clientException != null) {
                        exception = clientException
                        clientException.printStackTrace()
                        status = "客户端异常"
                        Log.d(TAG, status)
                    } else {
                        exception = serviceException!!
                        status = "服务端异常, 错误码: ${serviceException?.errorCode}"
                        Log.d(TAG, status)
                    }
                    Log.d(TAG, "初始化失败")
                    onFailure?.let { it("初始化失败未能找到 ${request?.bucketName} 的信息，$status", exception) }
                }

            })
    }

    override fun uploadFile(
        bucketName: String,
        key: String,
        filePath: String,
        progressListener: ((Int, Long, Long) -> Unit)?,
        callback: CloudStoreCallback?,
        isBigFile: Boolean
    ) {
        val progressListener = object : OSSProgressCallback<PutObjectRequest> {
            override fun onProgress(
                request: PutObjectRequest?,
                currentSize: Long,
                totalSize: Long
            ) {
                val progress = currentSize.times(100.0).div(totalSize).toInt()
                progressListener?.invoke(progress, currentSize, totalSize)
            }
        }
        if (!isBigFile) {
            val putObjectRequest =
                PutObjectRequest(bucketName, key, filePath)
            putObjectRequest.progressCallback = progressListener
            ossClient.asyncPutObject(
                putObjectRequest,
                object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
                    override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                        Log.d(TAG, "上传成功")
                        callback?.onSuccess()
                    }

                    override fun onFailure(
                        request: PutObjectRequest?,
                        clientException: ClientException?,
                        serviceException: ServiceException?
                    ) {
                        Log.d(TAG, "上传失败")
                        callback?.onFailed()
                    }

                })
        }
    }

    override fun downloadFile(
        bucketName: String,
        key: String,
        progressListener: CloudStoreProgressListener?,
        isBigFile: Boolean
    ): CloudStoreObject? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun downloadFile(
        bucketName: String,
        key: String,
        fileName: String,
        progressListener: CloudStoreProgressListener?,
        isBigFile: Boolean
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}