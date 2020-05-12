package online.mengchen.collectionhelper.data.source

import online.mengchen.collectionhelper.domain.entity.BookMark
import online.mengchen.collectionhelper.domain.model.BookMarkInfo

interface BookMarkDataSource {
    
    fun getBookMarks(page: Int, size: Int, callback: LoadedDataCallback<BookMarkInfo>)

    fun saveBookMark(bookMark: BookMark)

    fun isLastPage(): Boolean

}