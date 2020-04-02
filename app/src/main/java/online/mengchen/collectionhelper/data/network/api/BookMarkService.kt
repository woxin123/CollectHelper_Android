package online.mengchen.collectionhelper.data.network.api

import androidx.lifecycle.LiveData
import online.mengchen.collectionhelper.bookmark.AddBookMark
import online.mengchen.collectionhelper.bookmark.BookMark
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.Page
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BookMarkService {
    companion object {
        const val BOOKMARKS = "/bookmarks"
    }
    @GET(BOOKMARKS)
    suspend fun getBookMark(@Query("page") page: Int, @Query("size") pageSize: Int): ApiResult<Page<BookMark>>

    @POST(BOOKMARKS)
    suspend fun addBookMark(@Body addBookMark: AddBookMark): ApiResult<BookMark>
}