package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aliyun_config")
data class AliyunConfig (
    @PrimaryKey var id: Long? = null,
    @ColumnInfo(name = "access_key") var accessKey: String,
    @ColumnInfo(name = "secret_key") var secretKey: String,
    @ColumnInfo(name = "bucket") var bucket: String,
    @ColumnInfo(name = "uid") var uid: Long
)