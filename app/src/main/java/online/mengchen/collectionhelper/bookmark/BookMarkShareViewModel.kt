package online.mengchen.collectionhelper.bookmark

import android.os.Build
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

class BookMarkShareViewModel : ViewModel() {

    val bookMarkService = RetrofitClient.bookMarkService
    val addBookMarkStatus: MutableLiveData<ApiResult<BookMark>> = MutableLiveData()
    val bookMarkCategories: MutableLiveData<ApiResult<List<BookMarkCategory>>> = MutableLiveData()

    fun addBookMark(categoryId: Long = -1, url: String) {
        viewModelScope.launch {
            val addBookMarkRes = withContext(Dispatchers.IO) {
                bookMarkService.addBookMark(AddBookMark(categoryId, url))
            }
            addBookMarkStatus.value = addBookMarkRes
        }
    }

    fun getBookMarkCategories() {
        viewModelScope.launch {
            val bookMarkCategoriesRes = withContext(Dispatchers.IO) {
                bookMarkService.getBookMarkCategories()
            }
            bookMarkCategories.value = bookMarkCategoriesRes
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

}