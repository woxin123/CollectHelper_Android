package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "file_info")
data class FileInfo(
    @PrimaryKey(autoGenerate = true) var fid: Long? = null,
    @ColumnInfo(name = "key") var key: String,
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "file_type") var fileType: Int,
    @ColumnInfo(name = "store_type") var storeType: Int,
    @ColumnInfo(name = "category_id") var categoryId: Int,
    @ColumnInfo(name = "uid") var uid: Long,
    @ColumnInfo(name = "createTime") var createTime: LocalDateTime
)