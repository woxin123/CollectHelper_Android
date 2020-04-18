package online.mengchen.collectionhelper.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import online.mengchen.collectionhelper.domain.entity.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE uid = :uid")
    suspend fun findById(uid: Long): LiveData<User>

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)
}