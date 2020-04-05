package online.mengchen.collectionhelper.bookmark

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.data.network.RetrofitClient
import retrofit2.HttpException

class BookMarkShareViewModel : ViewModel() {

    companion object {
        const val TAG = "BookMarkShareViewModel"
    }

    private val bookMarkService = RetrofitClient.bookMarkService
    val addBookMarkStatus: MutableLiveData<ApiResult<BookMark>> = MutableLiveData()
    val bookMarkCategories: MutableLiveData<ApiResult<List<BookMarkCategory>>> = MutableLiveData()
    val addBookMarkCategory by lazy { MutableLiveData<ApiResult<BookMarkCategory>>() }


    fun getBookMarkCategories() {
        viewModelScope.launch {
            try {
                val bookMarkCategoriesRes = withContext(Dispatchers.IO) {
                    bookMarkService.getBookMarkCategories()
                }
                bookMarkCategories.value = bookMarkCategoriesRes
            } catch (e: HttpException) {
                val status = e.code()
                val message = e.message()
                Log.d(TAG, "HTTP Exception status = $status And message = $message")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addBookMark(bookMarkCategories: MutableList<BookMarkCategory>, url: String) {
        viewModelScope.launch {
            val bookMark = AddBookMark(url = url)
            if (bookMarkCategories.isEmpty()) {
                bookMarkCategories.add(BookMarkCategory.unCategorized())
            }
            var addBookMarkRes: ApiResult<BookMark>? = null
            withContext(Dispatchers.IO) {
                bookMarkCategories.forEach {
                    bookMark.categoryId = it.categoryId
                    addBookMarkRes = bookMarkService.addBookMark(bookMark)
                }
            }
            addBookMarkStatus.value = addBookMarkRes
        }
    }

    fun addBookMarkCategory(bookMarkCategoryName: String) {
        try {
            viewModelScope.launch {
                val addBookMarkCategoryRes = withContext(Dispatchers.IO) {
                    bookMarkService.addBookMarkCategory(AddOrUpdateBookMarkCategory(categoryName = bookMarkCategoryName))
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