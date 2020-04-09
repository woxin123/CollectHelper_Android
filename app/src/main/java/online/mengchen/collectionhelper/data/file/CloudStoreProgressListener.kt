package online.mengchen.collectionhelper.data.file

interface CloudStoreProgressListener {

    fun progressChange(progress: Int, currentBytes: Long?, totalBytes: Long?)

}