package online.mengchen.collectionhelper.data.file.aliyun

import online.mengchen.collectionhelper.data.CloudStoreConfiguration
import javax.crypto.SecretKey

class AliyunConfiguration(override val accessKey: String,
                          override val secretKey: String,
                          val bucket: String) : CloudStoreConfiguration {
}