package online.mengchen.collectionhelper.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import online.mengchen.collectionhelper.bookmark.BookMarkDetail


@Dao
interface BookMarkDetailDao {

    @Query("SELECT * FROM book_mark_detail")
    suspend fun getAll(): LiveData<List<BookMarkDetail>>

    @Query("SELeCT * FROM book_mark_detail WHERE id = :id")
    suspend fun findById(id: Long): BookMarkDetail?

    @Insert
    suspend fun insert(bookMarkDetail: BookMarkDetail)

    @Update
    suspend fun update(bookMarkDetail: BookMarkDetail)

    @Delete
    suspend fun delete(bookMarkDetail: BookMarkDetail)

}