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
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import retrofit2.HttpException

class BookMarkViewModel : ViewModel() {

    private val bookMarkService = RetrofitClient.bookMarkService


    val bookMarkCount: ObservableField<String> = ObservableField("0")
    lateinit var bookMarkAdapter: BookMarkAdapter
    val refreshBookMarks: MutableLiveData<ApiResult<Page<BookMark>>> = MutableLiveData()
    val loadMoreLiveData: MutableLiveData<ApiResult<Page<BookMark>>> = MutableLiveData()

    /**
     * @param refresh 是否是刷新， true 刷新，false load more
     */
    fun getBookMarks(refresh: Boolean, pageNo: Int = 0, pageSize: Int = 10) {
//        val bookMarksLiveData: LiveData<ApiResult<Page<BookMark>>> =
//            if (refresh) {
//                bookMarkService.getBookMark(0, pageSize)
//            } else {
//                bookMarkService.getBookMark(pageNo, pageSize)
//            }
//        val res = bookMarksLiveDat
//        if (res.status == 200) {
//            pageNo = res.data?.number!!
//            pageSize = res.data?.size!!
//        }
//        viewModelScope.launch {
//            getBookMarkDetails(res.data?.content!!)
//        }
//        return bookMarksLiveData
        var bookMarksRes: ApiResult<Page<BookMark>>? = null
        viewModelScope.launch {
            try {
                bookMarksRes = withContext(Dispatchers.IO) {
                    if (refresh) {
                        bookMarkService.getBookMark(0, pageSize)
                    } else {
                        bookMarkService.getBookMark(pageNo, pageSize)
                    }
                }
            } catch (e: HttpException) {
                HttpExceptionProcess.process(e)
            }
            bookMarksRes?.data?.content?.let { getBookMarkDetails(it) }
            if (refresh) {
                refreshBookMarks.value = bookMarksRes
            } else {
                loadMoreLiveData.value = bookMarksRes
            }
        }
    }

    fun addBooMarks(bookMarks: List<BookMark>, refresh: Boolean = true) {
        viewModelScope.launch {
            getBookMarkDetails(bookMarks)
            if (refresh) {
                bookMarkAdapter.data = mutableListOf(*bookMarks.toTypedArray())
            } else {
                bookMarkAdapter.addAll(listOf(*bookMarks.toTypedArray()))
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