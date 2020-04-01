package online.mengchen.collectionhelper.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.data.network.RetrofitClient

class BookMarkShareViewModel : ViewModel() {

    val bookMarkService = RetrofitClient.bookMarkService

    fun addBookMark(categoryId: Long = -1, url: String): LiveData<ApiResult<BookMark>> {
        return bookMarkService.addBookMark(AddBookMark(categoryId, url))
    }

}