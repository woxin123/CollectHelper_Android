package online.mengchen.collectionhelper.bookmark

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.domain.entity.Category
import retrofit2.HttpException

class BookMarkShareViewModel : ViewModel() {

    companion object {
        const val TAG = "BookMarkShareViewModel"
    }

    private val bookMarkService = RetrofitClient.bookMarkService
    private val categoryService = RetrofitClient.categoryService
    val addBookMarkInfoStatus: MutableLiveData<ApiResult<BookMarkInfo>> = MutableLiveData()
    val categories: MutableLiveData<ApiResult<List<CategoryInfo>>> = MutableLiveData()
    val addBookMarkCategory by lazy { MutableLiveData<ApiResult<CategoryInfo>>() }


    fun getBookMarkCategories() {
        viewModelScope.launch {
            try {
                val bookMarkCategoriesRes = withContext(Dispatchers.IO) {
                    categoryService.getBookMarkCategories()
                }
                categories.value = bookMarkCategoriesRes
            } catch (e: HttpException) {
                val status = e.code()
                val message = e.message()
                Log.d(TAG, "HTTP Exception status = $status And message = $message")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addBookMark(categoryIfs: MutableList<CategoryInfo>, url: String) {
        viewModelScope.launch {
            val bookMark = AddBookMark(url = url)
            if (categoryIfs.isEmpty()) {
                categoryIfs.add(CategoryInfo.unCategorized(Category.BOOKMARK))
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

    fun addBookMarkCategory(bookMarkCategoryName: String) {
        try {
            viewModelScope.launch {
                val addBookMarkCategoryRes = withContext(Dispatchers.IO) {
                    categoryService.addCategory(AddOrUpdateCategory(categoryName = bookMarkCategoryName, categoryType = Category.BOOKMARK))
                }
                addBookMarkCategory.value = addBookMarkCategoryRes
            }
        } catch (e: HttpException) {
            val status = e.code()
            val message = e.message()
            Log.d(TAG, "HTTP Exception status = $status message = $message")
        }
    }

}