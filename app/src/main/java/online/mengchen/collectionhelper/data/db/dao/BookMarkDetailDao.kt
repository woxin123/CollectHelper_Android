package online.mengchen.collectionhelper.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import online.mengchen.collectionhelper.domain.entity.BookMarkDetail


@Dao
interface BookMarkDetailDao {

    @Query("SELECT * FROM book_mark_detail")
    fun getAll(): LiveData<List<BookMarkDetail>>

    @Query("SELECT count(*) FROM book_mark_detail WHERE id = :id")
    suspend fun existsById(id: Long): Boolean

    @Query("SELeCT * FROM book_mark_detail WHERE id = :id")
    suspend fun findById(id: Long): BookMarkDetail?

    @Query("SELECT * FROM book_mark_detail WHERE bid = :bid")
    suspend fun findByBookMarkId(bid: Long): BookMarkDetail?

    @Insert
    suspend fun insert(bookMarkDetail: BookMarkDetail)

    @Update
    suspend fun update(bookMarkDetail: BookMarkDetail)

    @Delete
    suspend fun delete(bookMarkDetail: BookMarkDetail)

    @Query("DELETE FROM book_mark_detail")
    suspend fun deleteAll()

}