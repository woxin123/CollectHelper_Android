package online.mengchen.collectionhelper.data.file

import online.mengchen.collectionhelper.common.FileType

interface CloudStore {

    val cfg: CloudStoreConfiguration

    fun uploadFile(
        bucketName: String,
        key: String,
        filePath: String,
        fileType: Int = FileType.IMAGE,
        progressListener: ((Int, Long, Long) -> Unit)? = null,
        callback: CloudStoreCallback? = null,
        isBigFile: Boolean = false
    )

    fun downloadFile(
        bucketName: String,
        key: String,
        callback: CloudStoreCallback,
        progressListener: CloudStoreProgressListener? = null,
        isBigFile: Boolean
    )

    fun downloadFile(
        bucketName: String,
        key: String,
        filePath: String,
        callback: CloudStoreCallback?,
        progressListener: CloudStoreProgressListener? = null,
        isBigFile: Boolean
    )

    fun getFileUrl(key: String): String?
}