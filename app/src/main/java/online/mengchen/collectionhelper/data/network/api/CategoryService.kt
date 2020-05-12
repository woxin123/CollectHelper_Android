package online.mengchen.collectionhelper.data.network.api

import online.mengchen.collectionhelper.domain.model.AddOrUpdateCategory
import online.mengchen.collectionhelper.domain.model.CategoryInfo
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.domain.entity.Category
import retrofit2.http.*

interface CategoryService {
    companion object {
        const val CATEGORIES = "categories"
    }

    @POST(CATEGORIES)
    suspend fun addCategory(@Body category: AddOrUpdateCategory): ApiResult<CategoryInfo>


    @GET("$CATEGORIES/{categoryType}")
    suspend fun getCategory(
        @Path("categoryType") @Category.CategoryType categoryType: Int,
        @Query("storeType") @StoreType.TypeStore storeType: Int
    ): ApiResult<List<CategoryInfo>>
}