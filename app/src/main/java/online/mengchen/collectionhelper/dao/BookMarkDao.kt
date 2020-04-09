package online.mengchen.collectionhelper.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import online.mengchen.collectionhelper.domain.entity.BookMark

@Dao
interface BookMarkDao {

    @Query("SELECT * FROM bookmark")
    fun getAll(): LiveData<List<BookMark>>

    @Query("SELECT * FROM bookmark WHERE id = :id")
    suspend fun findById(id: Long): BookMark?

    @Insert
    suspend fun insert(bookMark: BookMark)

    @Delete
    suspend fun delete(bookMark: BookMark)
}