package online.mengchen.collectionhelper.data.source

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.mengchen.collectionhelper.data.db.dao.BookMarkDao
import online.mengchen.collectionhelper.data.db.dao.BookMarkDetailDao
import online.mengchen.collectionhelper.data.db.dao.CategoryDao
import online.mengchen.collectionhelper.domain.entity.BookMark
import online.mengchen.collectionhelper.domain.entity.BookMarkDetail
import online.mengchen.collectionhelper.domain.model.BookMarkDetailInfo
import online.mengchen.collectionhelper.domain.model.BookMarkInfo
import online.mengchen.collectionhelper.utils.BookMarkUtils
import online.mengchen.collectionhelper.domain.model.CategoryInfo

class BookMarkLocalDataSource(
    private val bookMarkDao: BookMarkDao,
    private val bookMarkDetailDao: BookMarkDetailDao,
    private val categoryDao: CategoryDao,
    private val scope: CoroutineScope
) : BookMarkDataSource {

    private var lastPage = false

    override fun getBookMarks(page: Int, size: Int, callback: LoadedDataCallback<BookMarkInfo>) {
        scope.launch {
            val bookMarks = bookMarkDao.getBookMarks(page, size)
            if (bookMarks.isEmpty()) {
                lastPage = true
            }
            bookMarks.map {
                val bookMarkDetailInfo = if (it.detailId == null) {
                    BookMarkUtils.parseUrlToBookMark(it.url).apply {
                        saveBookMarkDetail(this!!, it)
                    }
                } else {
                    val bookMarkDetail = bookMarkDetailDao.findById(it.detailId!!)
                    BookMarkDetailInfo(
                        bookMarkDetail?.id!!,
                        bookMarkDetail.title,
                        bookMarkDetail.summary ?: "",
                        bookMarkDetail.icon ?: ""
                    )
                }
                val category = categoryDao.findById(it.categoryId)
                if (category == null) {
                    callback.onDataNotAvailable()
                    return@launch
                }
                val categoryInfo =
                    CategoryInfo(
                        category.cid!!,
                        category.categoryName,
                        category.categoryType,
                        category.createTime,
                        category.updateTime
                    )
                BookMarkInfo(
                    it.id!!,
                    it.url,
                    bookMarkDetailInfo,
                    categoryInfo,
                    it.createTime
                )
            }
        }
    }

    private fun saveBookMarkDetail(info: BookMarkDetailInfo, bookMark: BookMark) {
        scope.launch {
            val bookMarkDetail = BookMarkDetail(null, info.title, info.summary, info.icon, bookMark.id!!)
            bookMarkDetailDao.insert(bookMarkDetail)
            bookMark.detailId = bookMarkDetail.id
            bookMarkDao.update(bookMark)
        }
    }

    override fun saveBookMark(bookMark: BookMark) {
        scope.launch {
            bookMarkDao.insert(bookMark)
        }
    }

    override fun isLastPage(): Boolean {
        return lastPage
    }

}