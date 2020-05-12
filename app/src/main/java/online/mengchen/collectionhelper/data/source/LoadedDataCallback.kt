package online.mengchen.collectionhelper.data.source

interface LoadedDataCallback<T> {
    fun onDataLoaded(data: List<T>)

    fun onDataNotAvailable()

    fun onException(e: Exception)
}