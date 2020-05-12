package online.mengchen.collectionhelper.data.repository

import android.util.Log
import online.mengchen.collectionhelper.domain.model.BookMarkInfo
import online.mengchen.collectionhelper.common.StoreType
import online.mengchen.collectionhelper.data.db.dao.BookMarkDao
import online.mengchen.collectionhelper.data.db.dao.BookMarkDetailDao
import online.mengchen.collectionhelper.data.db.dao.CategoryDao
import online.mengchen.collectionhelper.domain.entity.BookMark
import online.mengchen.collectionhelper.domain.entity.BookMarkDetail
import online.mengchen.collectionhelper.utils.BookMarkUtils

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
            Log.d("mengchen", it.toString())
            val categoryInfo = categoryDao.findById(it.categoryId).getCategoryInfo()
            val bookMarkDetailInfo = getBookMarkDetail(it)?.getBookMarkDetailInfo()
            if (bookMarkDetailInfo == null) {
                Log.d("mengchenaa", "null")
            }
            BookMarkInfo(
                it.id!!,
                it.url,
                bookMarkDetailInfo,
                categoryInfo,
                it.createTime
            )
        }
    }

    suspend fun getBookMarkDetail(bookMark: BookMark): BookMarkDetail? {
        if (bookMark.detailId != null) {
            return bookMarkDetailDao.findById(bookMark.detailId!!)
        } else {
            var bookMarkDetail = bookMarkDetailDao.findByBookMarkId(bookMark.uid)
            if (bookMarkDetail == null) {
                bookMarkDetail = BookMarkUtils.parseUrlToBookMark(bookMark.url).let {
                    if (it == null) {
                        null
                    } else {
                        val res = BookMarkDetail(null, it.title, it.summary, it.icon, bookMark.id!!)
                        bookMarkDetailDao.insert(res)
                        bookMark.detailId = res.id
                        bookmarkDao.update(bookMark)
                        res
                    }
                }
            }
            return bookMarkDetail

        }
    }

    suspend fun insert(bookMark: BookMark) {
        bookmarkDao.findById(bookMark.id!!) ?: return
        bookmarkDao.insert(bookMark)
    }

    suspend fun insertBookMarkInfo(bookMarkInfo: BookMarkInfo) {
        Log.d("mengchenaa", bookMarkInfo.toString())
        if (!bookmarkDao.existsById(bookMarkInfo.id)) {
            bookmarkDao.insert(bookMarkInfo.getBookMark())
        }
        if (!categoryDao.existsById(bookMarkInfo.category.categoryId)) {
            categoryDao.insert(bookMarkInfo.category.getCategory(StoreType.ALIYUN))
        }
        if (bookMarkInfo.bookMarkDetail != null) {
            if (bookMarkInfo.bookMarkDetail!!.id == null || !bookMarkDetailDao.existsById(bookMarkInfo.bookMarkDetail!!.id!!)) {
                Log.d("mengchenaa", bookMarkInfo.bookMarkDetail.toString())
                bookMarkDetailDao.insert(bookMarkInfo.bookMarkDetail!!.getBookMarkDetail())
            }
        }
    }

}