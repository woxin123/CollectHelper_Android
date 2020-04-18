package online.mengchen.collectionhelper.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import online.mengchen.collectionhelper.domain.entity.BookMark

@Dao
interface BookMarkDao {

    @Query("SELECT * FROM bookmark")
    suspend fun getAll(): List<BookMark>

    @Query("SELECT * FROM bookmark WHERE id = :id")
    suspend fun findById(id: Long): BookMark?

    @Query("SELECT count(*) FROM bookmark WHERE id = :id")
    suspend fun existsById(id: Long): Boolean

    @Insert
    suspend fun insert(bookMark: BookMark)

    @Delete
    suspend fun delete(bookMark: BookMark)
}