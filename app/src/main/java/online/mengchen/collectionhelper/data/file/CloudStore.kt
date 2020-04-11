package online.mengchen.collectionhelper.data.file

import online.mengchen.collectionhelper.data.CloudStoreConfiguration
import java.io.InputStream

interface CloudStore {

    val cfg: CloudStoreConfiguration

    fun uploadFile(
        bucketName: String,
        key: String,
        filePath: String,
        progressListener: ((Int, Long, Long) -> Unit)? = null,
        callback: CloudStoreCallback? = null,
        isBigFile: Boolean = false
    )

    fun downloadFile(
        bucketName: String,
        key: String,
        progressListener: CloudStoreProgressListener? = null,
        isBigFile: Boolean
    ): CloudStoreObject?

    fun downloadFile(
        bucketName: String,
        key: String,
        fileName: String,
        progressListener: CloudStoreProgressListener? = null,
        isBigFile: Boolean
    )
}