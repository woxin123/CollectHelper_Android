package online.mengchen.collectionhelper.data.source


interface GetDataCallback<T> {
    fun onDataLoaded(data: T)
    fun onDataNotAvailable()
}