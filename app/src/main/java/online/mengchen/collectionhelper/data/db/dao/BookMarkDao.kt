package online.mengchen.collectionhelper.data.db.dao

import androidx.room.*
import online.mengchen.collectionhelper.domain.entity.BookMark

@Dao
interface BookMarkDao {

    @Query("SELECT * FROM bookmark")
    suspend fun getAll(): List<BookMark>

    @Query("SELECT * FROM bookmark ORDER BY create_time LIMIT :size OFFSET :page*:size")
    suspend fun getBookMarks(page: Int, size: Int): List<BookMark>

    @Query("SELECT * FROM bookmark WHERE id = :id")
    suspend fun findById(id: Long): BookMark?

    @Query("SELECT count(*) FROM bookmark WHERE id = :id")
    suspend fun existsById(id: Long): Boolean

    @Insert
    suspend fun insert(bookMark: BookMark)

    @Update
    suspend fun update(bookMark: BookMark)

    @Delete
    suspend fun delete(bookMark: BookMark)

    @Query("DELETE FROM bookmark")
    suspend fun deleteAll()
}