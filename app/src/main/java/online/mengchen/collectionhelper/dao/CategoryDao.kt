package online.mengchen.collectionhelper.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import online.mengchen.collectionhelper.domain.entity.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    suspend fun findById() : Category

    @Insert
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)
}