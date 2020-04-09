package online.mengchen.collectionhelper.repository

import androidx.lifecycle.LiveData
import online.mengchen.collectionhelper.bookmark.BookMarkInfo
import online.mengchen.collectionhelper.dao.BookMarkDao
import online.mengchen.collectionhelper.domain.entity.BookMark

class BookMarkRepository(
    private val bookmarkDao: BookMarkDao
) {

    suspend fun getAll(): LiveData<List<BookMarkRepository>> {

    }

    suspend fun insert(bookMark: BookMark) {
        bookmarkDao.findById(bookMark.id!!) ?: return
        bookmarkDao.insert(bookMark)
    }

    suspend fun insertBookMarkInfo(bookMarkInfo: BookMarkInfo) {

    }

}