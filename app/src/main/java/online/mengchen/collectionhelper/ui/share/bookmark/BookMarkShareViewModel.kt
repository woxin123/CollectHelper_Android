package online.mengchen.collectionhelper.ui.share.bookmark

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.domain.model.AddBookMark
import online.mengchen.collectionhelper.domain.model.AddOrUpdateCategory
import online.mengchen.collectionhelper.domain.model.BookMarkInfo
import online.mengchen.collectionhelper.domain.model.CategoryInfo
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.domain.entity.Category
import online.mengchen.collectionhelper.ui.share.common.ShareViewModel

class BookMarkShareViewModel(application: Application) : ShareViewModel(application) {

    companion object {
        const val TAG = "BookMarkShareViewModel"
    }

    private val bookMarkService = RetrofitClient.bookMarkService
    private val categoryService = RetrofitClient.categoryService

    override suspend fun getCategories(): ApiResult<List<CategoryInfo>> {
        return categoryService.getCategory(Category.BOOKMARK, StoreType.ALIYUN)
    }

    override fun getAddOrUpdateCategory(categoryName: String): AddOrUpdateCategory {
        return AddOrUpdateCategory(
            categoryType = Category.BOOKMARK,
            categoryName = categoryName,
            storeType = StoreType.ALIYUN
        )
    }

    val addBookMarkInfoStatus: MutableLiveData<ApiResult<BookMarkInfo>> = MutableLiveData()
    val categories: MutableLiveData<ApiResult<List<CategoryInfo>>> = MutableLiveData()




    @RequiresApi(Build.VERSION_CODES.O)
    fun addBookMark(categoryIfs: MutableList<CategoryInfo>, url: String) {
        viewModelScope.launch {
            val bookMark =
                AddBookMark(url = url)
            if (categoryIfs.isEmpty()) {
                categoryIfs.add(
                    CategoryInfo.unCategorized(
                        Category.BOOKMARK
                    )
                )
            }
            var addBookMarkInfoRes: ApiResult<BookMarkInfo>? = null
            withContext(Dispatchers.IO) {
                categoryIfs.forEach {
                    bookMark.categoryId = it.categoryId
                    addBookMarkInfoRes = bookMarkService.addBookMark(bookMark)
                }
            }
            addBookMarkInfoStatus.value = addBookMarkInfoRes
        }
    }

}