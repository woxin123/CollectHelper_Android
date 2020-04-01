package online.mengchen.collectionhelper.bookmark

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.Page
import online.mengchen.collectionhelper.data.network.RetrofitClient

class BookMarkViewModel: ViewModel() {

    private val bookMarkService = RetrofitClient.bookMarkService

    var pageNo: Int = 0
    var pageSize: Int = 10

    val loginSuccess: LiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    val bookMarkCount: ObservableField<String> = ObservableField("0")
    lateinit var bookMarkAdapter: BookMarkAdapter

    /**
     * @param refresh 是否是刷新， true 刷新，false load more
     */
    fun getBookMarks(refresh: Boolean): LiveData<ApiResult<Page<BookMark>>> {
        if (refresh) {
            return bookMarkService.getBookMark(0, pageSize)
        }
        if (bookMarkAdapter.data.size >= 10) {
            this.pageNo++
        }
        return bookMarkService.getBookMark(pageNo, pageSize)
    }

    fun addBooMarks(bookMarks: List<BookMark>, refresh: Boolean = true) {
        viewModelScope.launch {
            getBookMarkDetails(bookMarks)
            if (refresh) {
                bookMarkAdapter.data = mutableListOf(*bookMarks.toTypedArray())
            } else {
//                bookMarkAdapter.data
            }
            bookMarkCount.set(bookMarkAdapter.data.size.toString())
        }

    }

    private suspend fun getBookMarkDetails(bookMarks: List<BookMark>) {
        withContext(Dispatchers.IO) {
            bookMarks.forEach {
                if (it.bookMarkDetail == null) {
                    it.bookMarkDetail = BookMarkUtils.parseUrlToBookMark(it.url)
                }
            }
        }
    }


}