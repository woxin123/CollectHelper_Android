package online.mengchen.collectionhelper.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "bookmark")
data class BookMark(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "create_time") val createTime: LocalDateTime,
    @ColumnInfo(name = "detail_id") var detailId: Long? = null,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    @ColumnInfo(name = "uid") val uid: Long
)