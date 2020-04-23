package online.mengchen.collectionhelper.ui.share.document

import android.app.Application
import online.mengchen.collectionhelper.bookmark.AddOrUpdateCategory
import online.mengchen.collectionhelper.bookmark.CategoryInfo
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.domain.entity.Category
import online.mengchen.collectionhelper.ui.share.common.ShareViewModel

class DocumentShareViewModel(application: Application) : ShareViewModel(application) {

    private val categoryService = RetrofitClient.categoryService


    override suspend fun getCategories(): ApiResult<List<CategoryInfo>> {
        return categoryService.getDocumentCategory()
    }

    override fun getAddOrUpdateCategory(categoryName: String): AddOrUpdateCategory {
        return AddOrUpdateCategory(categoryType = Category.DOCUMENT, categoryName = categoryName)
    }

}