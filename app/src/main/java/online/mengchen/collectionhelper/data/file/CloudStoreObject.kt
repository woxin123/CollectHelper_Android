package online.mengchen.collectionhelper.data.file

import java.io.InputStream

data class CloudStoreObject(
    val key: String? = null,
    val bucketName: String? = null,
    val objectContent: InputStream? = null
)