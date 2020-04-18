package online.mengchen.collectionhelper.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import online.mengchen.collectionhelper.domain.entity.AliyunConfig

@Dao
interface AliyunConfigDao {

    @Query("SELECT * FROM aliyun_config WHERE uid = :uid")
    fun findByUid(uid: Long): LiveData<AliyunConfig>

    @Insert
    suspend fun insert(aliyunConfig: AliyunConfig)

    @Delete
    suspend fun delete(aliyunConfig: AliyunConfig)

    @Query("DELETE FROM aliyun_config")
    suspend fun deleteAll()

}