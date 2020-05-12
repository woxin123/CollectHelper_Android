package online.mengchen.collectionhelper.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import online.mengchen.collectionhelper.domain.entity.QiniuConfig

@Dao
interface QiNiuYunConfigDao {

    @Insert
    suspend fun insert(qiNiuYunConfig: QiniuConfig)

    @Update
    suspend fun update(qiNiuYunConfig: QiniuConfig)

    @Query("SELECT * FROM qiniu_config WHERE uid = :uid")
    suspend fun findByUid(uid: Long): QiniuConfig?

}