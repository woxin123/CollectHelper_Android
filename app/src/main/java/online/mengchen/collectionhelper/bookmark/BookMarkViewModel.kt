package online.mengchen.collectionhelper.bookmark

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.Page
import online.mengchen.collectionhelper.data.db.BookMarkDatabase
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.repository.BookMarkRepository
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import online.mengchen.collectionhelper.utils.LoginUtils
import online.mengchen.collectionhelper.domain.entity.BookMark as BookMarkInDB
import retrofit2.HttpException

class BookMarkViewModel(application: Application) : AndroidViewModel(application) {

    private val bookMarkService = RetrofitClient.bookMarkService
    private val bookMarkRepository: BookMarkRepository

    init {
        val bookMarkDao = BookMarkDatabase.getDatabase(application, viewModelScope).bookMarkDao()
        bookMarkRepository = BookMarkRepository(bookMarkDao)
    }


    val bookMarkCount: ObservableField<String> = ObservableField("0")
    lateinit var bookMarkAdapter: BookMarkAdapter
    val refreshBookMarksInfo: MutableLiveData<ApiResult<Page<BookMarkInfo>>> = MutableLiveData()
    val loadMoreLiveData: MutableLiveData<ApiResult<Page<BookMarkInfo>>> = MutableLiveData()

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
        var bookMarksResInfo: ApiResult<Page<BookMarkInfo>>? = null
        viewModelScope.launch {
            try {
                bookMarksResInfo = withContext(Dispatchers.IO) {
                    if (refresh) {
                        bookMarkService.getBookMark(0, pageSize)
                    } else {
                        bookMarkService.getBookMark(pageNo, pageSize)
                    }
                }
            } catch (e: HttpException) {
                HttpExceptionProcess.process(e)
            }
            bookMarksResInfo?.data?.content?.let { getBookMarkDetails(it) }
            if (refresh) {
                refreshBookMarksInfo.value = bookMarksResInfo
            } else {
                loadMoreLiveData.value = bookMarksResInfo
            }
            insertBookMarks(bookMarksResInfo?.data?.content!!)
        }
    }

    fun addBooMarks(bookMarkInfos: List<BookMarkInfo>, refresh: Boolean = true) {
        viewModelScope.launch {
            getBookMarkDetails(bookMarkInfos)
            if (refresh) {
                bookMarkAdapter.data = mutableListOf(*bookMarkInfos.toTypedArray())
            } else {
                bookMarkAdapter.addAll(listOf(*bookMarkInfos.toTypedArray()))
            }
            bookMarkCount.set(bookMarkAdapter.data.size.toString())
        }

    }


    private suspend fun getBookMarkDetails(bookMarkInfos: List<BookMarkInfo>) {
        withContext(Dispatchers.IO) {
            bookMarkInfos.forEach {
                if (it.bookMarkDetail == null) {
                    it.bookMarkDetail = BookMarkUtils.parseUrlToBookMark(it.url)
                }
            }
        }
    }

    private fun insertBookMarks(bookMarkInfos: List<BookMarkInfo>) {
        viewModelScope.launch {
            bookMarkInfos.forEach {
                bookMarkRepository.insert(
                    BookMarkInDB(
                        it.id,
                        it.url,
                        it.createTime,
                        it.bookMarkDetail?.id,
                        it.bookMarkCategory.categoryId,
                        LoginUtils.user?.userId!!
                    )
                )
            }
        }
    }
}