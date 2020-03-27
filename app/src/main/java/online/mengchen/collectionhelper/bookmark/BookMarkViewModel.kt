package online.mengchen.collectionhelper.bookmark

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.Page
import online.mengchen.collectionhelper.data.network.RetrofitClient

class BookMarkViewModel: ViewModel() {

    private val bookMarkService = RetrofitClient.bookMarkService

    val bookMarkCount: ObservableField<String> = ObservableField("0")
    lateinit var bookMarkAdapter: BookMarkAdapter

    fun getBookMarks(pageNo: Int, pageSize: Int): LiveData<ApiResult<Page<BookMark>>> {
        return bookMarkService.getBookMark()
    }

}