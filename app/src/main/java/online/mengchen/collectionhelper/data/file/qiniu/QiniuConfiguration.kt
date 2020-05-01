package online.mengchen.collectionhelper.data.file.qiniu

import online.mengchen.collectionhelper.data.file.CloudStoreConfiguration

class QiniuConfiguration(
    override val accessKey: String,
    override val secretKey: String,
    val bucket: String,
    val imageBucket: String?,
    val documentBucket: String?,
    val musicBucket: String?,
    val videoBucket: String?,
    val imagePath: String?,
    val documentPath: String?,
    val musicPath: String?,
    val videoPath: String?,
    val domain: String
) : CloudStoreConfiguration {

}