package online.mengchen.collectionhelper.data.network.api

import online.mengchen.collectionhelper.bookmark.AddBookMark
import online.mengchen.collectionhelper.bookmark.AddOrUpdateBookMarkCategory
import online.mengchen.collectionhelper.bookmark.BookMarkInfo
import online.mengchen.collectionhelper.bookmark.BookMarkCategory
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.Page
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BookMarkService {
    companion object {
        const val BOOKMARKS = "/bookmarks"
        const val BOOKMARK_CATEGORY = "/bookmarkCategories"
    }
    @GET(BOOKMARKS)
    suspend fun getBookMark(@Query("page") page: Int, @Query("size") pageSize: Int): ApiResult<Page<BookMarkInfo>>

    @POST(BOOKMARKS)
    suspend fun addBookMark(@Body addBookMark: AddBookMark): ApiResult<BookMarkInfo>

    @GET(BOOKMARK_CATEGORY)
    suspend fun getBookMarkCategories(): ApiResult<List<BookMarkCategory>>

    @POST(BOOKMARK_CATEGORY)
    suspend fun addBookMarkCategory(@Body bookMarkCategory: AddOrUpdateBookMarkCategory): ApiResult<BookMarkCategory>
}