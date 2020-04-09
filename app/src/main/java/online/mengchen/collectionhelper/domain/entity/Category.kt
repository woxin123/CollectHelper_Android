package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "category")
class Category(
    @PrimaryKey(autoGenerate = true) var cid: Long? = null,
    @ColumnInfo(name = "category_name") var categoryName: String,
    @ColumnInfo(name = "category_type") var categoryType: String,
    @ColumnInfo(name = "create_time") var createTime: LocalDateTime,
    @ColumnInfo(name = "update_time") var updateTime: LocalDateTime,
    @ColumnInfo(name = "uid") var uid: Long
)