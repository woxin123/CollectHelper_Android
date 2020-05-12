package online.mengchen.collectionhelper.data.file.tencent

import com.tencent.qcloud.core.auth.SessionQCloudCredentials
import online.mengchen.collectionhelper.data.file.CloudStoreConfiguration

/**
 * accessKey 相当于
 */
class TencentCOSConfiguration(
    val secretKeyId: String,
    val secretKey: String,
    val bucket: String,
    val region: String
) : CloudStoreConfiguration {

}