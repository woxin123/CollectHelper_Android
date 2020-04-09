package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "bookmark")
data class BookMark(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "create_time") val createTime: LocalDateTime,
    @ColumnInfo(name = "detail_id") val detailId: Long? = null,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    @ColumnInfo(name = "uid") val uid: Long
)