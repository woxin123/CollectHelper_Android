package online.mengchen.collectionhelper.data.source

import online.mengchen.collectionhelper.domain.model.BookMarkInfo
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import retrofit2.HttpException

class BookMarkManager(
    private val bookMarkLocalDataSource: BookMarkLocalDataSource,
    private val bookMarkRemoteDataSource: BookMarkRemoteDataSource
) {

    private val page: Int = 0
    private val size: Int = 10

    private val cacheIsDirty: Boolean = false

    private val cachedBookMarks: LinkedHashMap<Long, BookMarkInfo> = LinkedHashMap()

    fun getBookMarks(refresh: Boolean, callback: LoadedDataCallback<BookMarkInfo>) {
        if (cachedBookMarks.size >= (page + 1) * size && !cacheIsDirty) {
            callback.onDataLoaded(cachedBookMarks.values.toList().subList(page * size, size))
        }

        if (cacheIsDirty) {

        }
    }

    fun getFromRemoteDataSource(callback: LoadedDataCallback<BookMarkInfo>) {
        bookMarkRemoteDataSource.getBookMarks(page, size, object : LoadedDataCallback<BookMarkInfo> {
            override fun onDataLoaded(data: List<BookMarkInfo>) {

            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

            override fun onException(e: Exception) {
                HttpExceptionProcess.process(e as HttpException)
            }

        })
    }

//    private fun refreshCached(bookMarks: List<BookMarkInfo>) {
//        if
//    }

}