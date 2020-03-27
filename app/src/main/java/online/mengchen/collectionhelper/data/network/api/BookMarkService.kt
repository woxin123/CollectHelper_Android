package online.mengchen.collectionhelper.data.network.api

import androidx.lifecycle.LiveData
import online.mengchen.collectionhelper.bookmark.BookMark
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.Page
import retrofit2.http.GET
import retrofit2.http.POST

interface BookMarkService {
    companion object {
        const val BOOKMARKS = "/bookmarks"
    }
    @GET(BOOKMARKS)
    fun getBookMark(): LiveData<ApiResult<Page<BookMark>>>

    @POST(BOOKMARKS)
    fun addBookMark(): LiveData<ApiResult<BookMark>>
}