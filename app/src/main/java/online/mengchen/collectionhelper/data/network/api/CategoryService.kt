package online.mengchen.collectionhelper.data.network.api

import online.mengchen.collectionhelper.bookmark.AddOrUpdateCategory
import online.mengchen.collectionhelper.bookmark.CategoryInfo
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.domain.entity.BookMark
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CategoryService {
    companion object {
        const val CATEGORIES = "categories"
        const val BOOKMARK = "BOOKMARK"
        const val IMAGE = "IMAGE"
        const val MUSIC = "MUSIC"
    }

    @GET("$CATEGORIES/$BOOKMARK")
    suspend fun getBookMarkCategories(): ApiResult<List<CategoryInfo>>

    @POST(CATEGORIES)
    suspend fun addCategory(@Body category: AddOrUpdateCategory): ApiResult<CategoryInfo>

    @GET("$CATEGORIES/$IMAGE")
    suspend fun getImageCategory(): ApiResult<List<CategoryInfo>>

    @GET("$CATEGORIES/$MUSIC")
    suspend fun getMusicCategory(): ApiResult<List<CategoryInfo>>
}