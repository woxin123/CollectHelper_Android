package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tencent_cos_config")
data class TencentCOSConfig (
    @PrimaryKey var id: Long? = null,
    @ColumnInfo(name = "secret_id") var secretId: String,
    @ColumnInfo(name = "secret_key") var secretKey: String,
    @ColumnInfo(name = "bucket") var bucket: String,
    @ColumnInfo(name = "region") var region: String,
    @ColumnInfo(name = "uid") var uid: Long
)