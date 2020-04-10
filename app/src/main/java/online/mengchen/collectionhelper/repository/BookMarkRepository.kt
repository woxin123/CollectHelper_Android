package online.mengchen.collectionhelper.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import online.mengchen.collectionhelper.bookmark.BookMarkInfo
import online.mengchen.collectionhelper.dao.BookMarkDao
import online.mengchen.collectionhelper.dao.BookMarkDetailDao
import online.mengchen.collectionhelper.dao.CategoryDao
import online.mengchen.collectionhelper.domain.entity.BookMark

class BookMarkRepository(
    private val bookmarkDao: BookMarkDao,
    private val bookMarkDetailDao: BookMarkDetailDao,
    private val categoryDao: CategoryDao
) {

    /**
     * 查询输入 BookMarkInfo
     */
    suspend fun getAll(): List<BookMarkInfo> {
        return bookmarkDao.getAll().map {
            val categoryInfo = categoryDao.findById(it.categoryId).getCategoryInfo()
            val bookMarkDetailInfo = it.detailId?.let {detailId: Long ->
                bookMarkDetailDao.findById(detailId)?.getBookMarkDetailInfo()
            }
            BookMarkInfo(it.id!!, it.url, bookMarkDetailInfo, categoryInfo, it.createTime)
        }
    }

    suspend fun insert(bookMark: BookMark) {
        bookmarkDao.findById(bookMark.id!!) ?: return
        bookmarkDao.insert(bookMark)
    }

    suspend fun insertBookMarkInfo(bookMarkInfo: BookMarkInfo) {
        if (!bookmarkDao.existsById(bookMarkInfo.id)) {
            bookmarkDao.insert(bookMarkInfo.getBookMark())
        }
        if (!categoryDao.existsById(bookMarkInfo.category.categoryId)) {
            categoryDao.insert(bookMarkInfo.category.getCategory())
        }
        if (bookMarkInfo.bookMarkDetail != null) {
            if (bookMarkDetailDao.existsById(bookMarkInfo.bookMarkDetail!!.id)) {
                bookMarkDetailDao.insert(bookMarkInfo.bookMarkDetail!!.getBookMarkDetail())
            }
        }
    }

}