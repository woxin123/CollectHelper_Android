package online.mengchen.collectionhelper.data.source

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.common.HTTPStatus
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.domain.entity.BookMark
import online.mengchen.collectionhelper.domain.model.AddBookMark
import online.mengchen.collectionhelper.domain.model.BookMarkInfo
import retrofit2.HttpException


class BookMarkRemoteDataSource(private val scope: CoroutineScope): BookMarkDataSource {

    private val bookMarkService = RetrofitClient.bookMarkService

    private var lastPage = false

    override fun getBookMarks(page: Int, size: Int, callback: LoadedDataCallback<BookMarkInfo>) {
        scope.launch {
            try {
                val bookMarkRes =  bookMarkService.getBookMark(page, size)
                if (bookMarkRes.data != null) {
                    if (bookMarkRes.data!!.last) {
                        lastPage = true
                    }
                    callback.onDataLoaded(bookMarkRes.data!!.content)
                }
            } catch (e: HttpException) {
                callback.onException(e)
            }
        }
    }

    /**
     * 服务器不存储 bookMarkDetail，每次利用刷新利用爬虫解析
     */
    @Throws(HttpException::class)
    override fun saveBookMark(bookMark: BookMark) {
        scope.launch {
            try {
                val addBookMark =
                    AddBookMark(url = bookMark.url)
                val bookMarkInfoRes = bookMarkService.addBookMark(addBookMark)
                if (bookMarkInfoRes.status == HTTPStatus.CREATED.code) {
                    bookMark.id = bookMarkInfoRes.data!!.id
                }

            } catch (e: HttpException) {
                throw e
            }
        }
    }

    override fun isLastPage(): Boolean {
        return lastPage
    }

}