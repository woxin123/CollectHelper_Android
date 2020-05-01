package online.mengchen.collectionhelper.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import online.mengchen.collectionhelper.domain.entity.AliyunConfig

@Dao
interface AliyunConfigDao {

    @Query("SELECT * FROM aliyun_config WHERE uid = :uid")
    fun findByUid(uid: Long): LiveData<AliyunConfig?>

    @Query("SELECT * FROM aliyun_config WHERE uid = :uid")
    suspend fun findByUid2(uid: Long): AliyunConfig

    @Insert
    suspend fun insert(aliyunConfig: AliyunConfig)

    @Update
    suspend fun update(aliyunConfig: AliyunConfig)

    @Delete
    suspend fun delete(aliyunConfig: AliyunConfig)

    @Query("DELETE FROM aliyun_config")
    suspend fun deleteAll()

}