package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aliyun_config")
data class AliyunConfig (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "access_key") val accessKey: String,
    @ColumnInfo(name = "secret_key") val secretKey: String,
    @ColumnInfo(name = "bucket") val bucket: String,
    @ColumnInfo(name = "uid") val uid: Long
)