package online.mengchen.collectionhelper.bookmark

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.Page
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.domain.entity.BookMarkDetail
import online.mengchen.collectionhelper.repository.BookMarkRepository
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import retrofit2.HttpException

class BookMarkViewModel(application: Application) : AndroidViewModel(application) {

    private val bookMarkService = RetrofitClient.bookMarkService
    private val bookMarkRepository: BookMarkRepository

    init {
        val database = CollectHelpDatabase.getDatabase(application, viewModelScope)
        bookMarkRepository = BookMarkRepository(database.bookMarkDao(), database.bookMarkDetailDao(), database.categoryDao())
    }


    val bookMarkCount: ObservableField<String> = ObservableField("0")
    lateinit var bookMarkAdapter: BookMarkAdapter
    val refreshBookMarksInfo: MutableLiveData<ApiResult<Page<BookMarkInfo>>> = MutableLiveData()
    val loadMoreLiveData: MutableLiveData<ApiResult<Page<BookMarkInfo>>> = MutableLiveData()
    val loadBookMarks: MutableLiveData<List<BookMarkInfo>> = MutableLiveData()

    fun loadBookMarks() {
        viewModelScope.launch {
            val bookMarks = bookMarkRepository.getAll()
            if (bookMarks.isEmpty()) {
                getBookMarks(true)
            } else {
                loadBookMarks.value = bookMarks
            }
        }
    }

    /**
     * @param refresh 是否是刷新， true 刷新，false load more
     */
    fun getBookMarks(refresh: Boolean, pageNo: Int = 0, pageSize: Int = 10) {
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


    private suspend fun getBookMarkDetails(bookMarkIfs: List<BookMarkInfo>) {
        withContext(Dispatchers.IO) {
            bookMarkIfs.forEach {
                if (it.bookMarkDetail == null) {
                    it.bookMarkDetail = BookMarkUtils.parseUrlToBookMark(it.url)
                }
            }
        }
    }

    private fun insertBookMarks(bookMarkIfs: List<BookMarkInfo>) {
        viewModelScope.launch {
            bookMarkIfs.forEach {
               bookMarkRepository.insertBookMarkInfo(it)
            }
        }
    }
}