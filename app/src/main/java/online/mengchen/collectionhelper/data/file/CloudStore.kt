package online.mengchen.collectionhelper.data.file

import online.mengchen.collectionhelper.data.CloudStoreConfiguration

interface CloudStore {

    val configuration: CloudStoreConfiguration

    fun uploadFile(
        bucketName: String,
        key: String,
        progressListener: CloudStoreProgressListener? = null,
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