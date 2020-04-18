package online.mengchen.collectionhelper.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import online.mengchen.collectionhelper.domain.entity.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category WHERE cid = :cid")
    suspend fun findById(cid: Long) : Category

    @Query("SELECT count(*) FROM category WHERE cid = :cid")
    suspend fun existsById(cid: Long): Boolean

    @Insert
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)
}