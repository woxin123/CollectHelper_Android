package online.mengchen.collectionhelper.data.file

interface CloudStoreCallback {

    fun <T> onSuccess(t: T)

    fun onFailed()
}