package online.mengchen.collectionhelper.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import online.mengchen.collectionhelper.domain.entity.TencentCOSConfig

@Dao
interface TencentCOSConfigDao {

    @Query("SELECT * FROM tencent_cos_config WHERE uid = :uid")
    suspend fun findByUid(uid: Long): TencentCOSConfig?

    @Insert
    suspend fun insert(tencentCOSConfig: TencentCOSConfig)

    @Update
    suspend fun update(tencentCOSConfig: TencentCOSConfig)

}