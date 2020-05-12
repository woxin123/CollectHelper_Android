package online.mengchen.collectionhelper.ui.bookmark

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.mengchen.collectionhelper.common.ApiResult
import online.mengchen.collectionhelper.common.Page
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.db.CollectHelpDatabase
import online.mengchen.collectionhelper.data.network.RetrofitClient
import online.mengchen.collectionhelper.data.repository.BookMarkRepository
import online.mengchen.collectionhelper.domain.entity.BookMark
import online.mengchen.collectionhelper.domain.entity.Category
import online.mengchen.collectionhelper.domain.model.BookMarkInfo
import online.mengchen.collectionhelper.domain.model.CategoryInfo
import online.mengchen.collectionhelper.utils.BookMarkUtils
import online.mengchen.collectionhelper.utils.HttpExceptionProcess
import retrofit2.HttpException

class BookMarkViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "BookMarkViewModel"
    }

    private val bookMarkService = RetrofitClient.bookMarkService
    private val bookMarkRepository: BookMarkRepository
    private val categoryMap = mutableMapOf<Long, CategoryInfo>()


    init {
        val database = CollectHelpDatabase.getDatabase(application, viewModelScope)
        bookMarkRepository = BookMarkRepository(
            database.bookMarkDao(),
            database.bookMarkDetailDao(),
            database.categoryDao()
        )
        val categoryService = RetrofitClient.categoryService
        viewModelScope.launch {
            val categoryRes = categoryService.getCategory(Category.BOOKMARK, StoreType.ALIYUN)
            val categories = categoryRes.data!!
            for (category in categories) {
                categoryMap[category.categoryId] = category
            }
        }
    }


    val bookMarkCount: ObservableField<String> = ObservableField("0")
    lateinit var bookMarkAdapter: BookMarkAdapter
    val refreshBookMarksInfo: MutableLiveData<ApiResult<Page<BookMarkInfo>>> = MutableLiveData()
    val loadMoreLiveData: MutableLiveData<ApiResult<Page<BookMarkInfo>>> = MutableLiveData()
    val loadBookMarks: MutableLiveData<List<BookMarkInfo>> = MutableLiveData()

    fun loadBookMarks() {
        viewModelScope.launch {
            val bookMarks = bookMarkRepository.getAll()
            bookMarkCount.set(bookMarks.size.toString())
            if (bookMarks.isEmpty()) {
//                getBookMarks(true)
                Log.d(TAG, "没有数据")
            } else {
                loadBookMarks.value = bookMarks
            }
        }
    }

    suspend fun getBookMarkInfo(bookMarks: List<BookMark>): List<BookMarkInfo?> {
        val bookMarkInfoList = mutableListOf<BookMarkInfo>()
        bookMarks.forEach {
            val bookMarkDetail = bookMarkRepository.getBookMarkDetail(it)
            if (bookMarkDetail == null) {
                // 提示
            } else {
                bookMarkInfoList.add(
                    BookMarkInfo(
                        it.id!!,
                        it.url,
                        bookMarkDetail.getBookMarkDetailInfo(),
                        categoryMap[it.categoryId]!!,
                        it.createTime
                    )
                )
            }
        }
        return bookMarkInfoList
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
                bookMarkCount.set(bookMarksResInfo?.data?.totalElements!!.toString())
                refreshBookMarksInfo.value = bookMarksResInfo
            } else {
                loadMoreLiveData.value = bookMarksResInfo
            }
            bookMarksResInfo?.data!!.content.forEach {
                bookMarkRepository.insertBookMarkInfo(it)
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