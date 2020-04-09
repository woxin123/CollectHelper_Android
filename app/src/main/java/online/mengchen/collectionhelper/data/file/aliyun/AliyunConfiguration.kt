package online.mengchen.collectionhelper.data.file.aliyun

import online.mengchen.collectionhelper.data.CloudStoreConfiguration
import javax.crypto.SecretKey

class AliyunConfguration(override val accessKey: String,
                         override val secretKey: String) : CloudStoreConfiguration {

}